package umc.th.juinjang.domain.pencil.acquired

interface AcquiredPencilRepository {

    fun findAllByMemberIdOrderByCreatedAtDesc(memberId: Long): List<AcquiredPencil>

    fun findById(id: Long): AcquiredPencil?

    fun existsByMemberIdAndIsReadFalse(memberId: Long): Boolean

    fun existsByMemberId(memberId: Long): Boolean

    fun save(acquiredPencil: AcquiredPencil): AcquiredPencil
}
