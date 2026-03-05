package umc.th.juinjang.application.facade.acquired

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import umc.th.juinjang.application.service.acquired.AcquiredPencilService
import umc.th.juinjang.domain.note.shared.repository.SharedNoteRepository

@Service
class AcquiredPencilFacade(
    private val acquiredPencilService: AcquiredPencilService,
    // TODO: SharedNote Kotlin 리팩토링 후 Service로 교체
    private val sharedNoteRepository: SharedNoteRepository,
) {

    @Transactional(readOnly = true)
    fun getAcquiredPencils(memberId: Long): List<AcquiredPencilResult> {
        val pencils = acquiredPencilService.getAcquiredPencilsByMember(memberId)

        val sharedNoteIds = pencils.mapNotNull { it.sharedNoteId }
        val buildingNameMap = if (sharedNoteIds.isNotEmpty()) {
            sharedNoteRepository.findBuildingNameMapByIds(sharedNoteIds)
        } else {
            emptyMap()
        }

        return pencils.map { AcquiredPencilResult.from(it, buildingNameMap[it.sharedNoteId]) }
    }

}
