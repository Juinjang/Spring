package umc.th.juinjang.application.facade.acquired

import umc.th.juinjang.domain.pencil.acquired.AcquiredPencil
import umc.th.juinjang.domain.pencil.acquired.AcquiredType
import java.time.LocalDateTime

data class AcquiredPencilResult(
    val acquiredPencilId: Long,
    val content: String,
    val sharedNoteId: Long?,
    val acquiredQuantity: Long,
    val buildingName: String?,
    val isRead: Boolean,
    val type: AcquiredType,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(pencil: AcquiredPencil, buildingName: String?): AcquiredPencilResult =
            AcquiredPencilResult(
                acquiredPencilId = pencil.id!!,
                content = pencil.content!!,
                sharedNoteId = pencil.sharedNoteId,
                acquiredQuantity = pencil.acquiredQuantity!!,
                buildingName = buildingName,
                isRead = pencil.isRead!!,
                type = pencil.type!!,
                createdAt = pencil.createdAt,
            )
    }
}
