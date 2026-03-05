package umc.th.juinjang.domain.pencil.acquired

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import umc.th.juinjang.domain.common.BaseEntity
import java.time.LocalDateTime

@Entity
@Table(name = "acquired_pencil")
open class AcquiredPencil : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acquired_pencil_id")
    var id: Long? = null
        protected set

    @Column(name = "member_id", nullable = false)
    var refMemberId: Long? = null
        protected set

    @Column(nullable = false)
    var content: String? = null
        protected set

    var sharedNoteId: Long? = null
        protected set

    @Column(nullable = false)
    var acquiredQuantity: Long? = null
        protected set

    @Column(nullable = false)
    var isRead: Boolean? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: AcquiredType? = null
        protected set

    fun markAsRead() {
        this.isRead = true
    }

    companion object {
        @JvmStatic
        fun create(
            memberId: Long,
            content: String,
            sharedNoteId: Long?,
            acquiredQuantity: Long,
            type: AcquiredType,
        ): AcquiredPencil = AcquiredPencil().apply {
            this.refMemberId = memberId
            this.content = content
            this.sharedNoteId = sharedNoteId
            this.acquiredQuantity = acquiredQuantity
            this.isRead = false
            this.type = type
        }

        @JvmStatic
        fun createWithDate(
            memberId: Long,
            content: String,
            sharedNoteId: Long?,
            acquiredQuantity: Long,
            type: AcquiredType,
            createdAt: LocalDateTime,
        ): AcquiredPencil = AcquiredPencil().apply {
            this.refMemberId = memberId
            this.content = content
            this.sharedNoteId = sharedNoteId
            this.acquiredQuantity = acquiredQuantity
            this.isRead = false
            this.type = type
            setCreatedAt(createdAt)
        }
    }
}
