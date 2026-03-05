package umc.th.juinjang.infrastructure.pencil.acquired

import org.springframework.data.jpa.repository.JpaRepository
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencil

interface AcquiredPencilJpaRepository : JpaRepository<AcquiredPencil, Long> {

    fun findAllByRefMemberIdOrderByCreatedAtDesc(memberId: Long): List<AcquiredPencil>

    fun existsByRefMemberIdAndIsReadFalse(memberId: Long): Boolean

    fun existsByRefMemberId(memberId: Long): Boolean
}
