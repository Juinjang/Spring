package umc.th.juinjang.application.service.acquired

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencil
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencilRepository

@Service
class AcquiredPencilService(private val acquiredPencilRepository: AcquiredPencilRepository) {

    @Transactional(readOnly = true)
    fun getAcquiredPencilsByMember(memberId: Long): List<AcquiredPencil> =
        acquiredPencilRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId)

    fun findById(acquiredPencilId: Long): AcquiredPencil =
        acquiredPencilRepository.findById(acquiredPencilId)
            ?: throw EntityNotFoundException("AcquiredPencil not found with id: $acquiredPencilId")

    fun existsByMemberIdAndIsReadFalse(memberId: Long): Boolean =
        acquiredPencilRepository.existsByMemberIdAndIsReadFalse(memberId)

    @Transactional
    fun markAsRead(acquiredPencilId: Long): Boolean {
        val acquiredPencil = acquiredPencilRepository.findById(acquiredPencilId)
            ?: throw EntityNotFoundException("AcquiredPencil not found with id: $acquiredPencilId")

        acquiredPencil.markAsRead()
        return true
    }

    @Transactional(readOnly = true)
    fun isAllRead(memberId: Long): Boolean = !acquiredPencilRepository.existsByMemberIdAndIsReadFalse(memberId)
}
