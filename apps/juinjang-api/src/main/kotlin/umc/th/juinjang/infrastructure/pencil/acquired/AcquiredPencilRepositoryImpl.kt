package umc.th.juinjang.infrastructure.pencil.acquired

import org.springframework.stereotype.Component
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencil
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencilRepository

@Component
class AcquiredPencilRepositoryImpl(private val jpaRepository: AcquiredPencilJpaRepository) : AcquiredPencilRepository {

    override fun findAllByMemberIdOrderByCreatedAtDesc(memberId: Long): List<AcquiredPencil> =
        jpaRepository.findAllByRefMemberIdOrderByCreatedAtDesc(
            memberId,
        )

    override fun findById(id: Long): AcquiredPencil? = jpaRepository.findById(id).orElse(null)

    override fun existsByMemberIdAndIsReadFalse(memberId: Long): Boolean = jpaRepository.existsByRefMemberIdAndIsReadFalse(
        memberId,
    )

    override fun existsByMemberId(memberId: Long): Boolean = jpaRepository.existsByRefMemberId(memberId)

    override fun save(acquiredPencil: AcquiredPencil): AcquiredPencil = jpaRepository.save(acquiredPencil)
}
