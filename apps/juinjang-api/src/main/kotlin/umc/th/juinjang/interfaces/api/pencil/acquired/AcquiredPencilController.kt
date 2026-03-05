package umc.th.juinjang.interfaces.api.pencil.acquired

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import umc.th.juinjang.api.dto.ApiResponse
import umc.th.juinjang.application.facade.acquired.AcquiredPencilFacade
import umc.th.juinjang.application.service.acquired.AcquiredPencilService

@RestController
@RequestMapping("/api/v2/pencil")
class AcquiredPencilController(
    private val acquiredPencilFacade: AcquiredPencilFacade,
    private val acquiredPencilService: AcquiredPencilService,
) : AcquiredApiSpec {

    @GetMapping("/acquired")
    override fun getAcquiredPencils(
        @AuthenticationPrincipal(expression = "memberId") memberId: Long,
    ): ApiResponse<List<AcquiredDto.List>> {
        val pencils = acquiredPencilFacade.getAcquiredPencils(memberId)
            .map { AcquiredDto.List.from(it) }
        return ApiResponse.onSuccess(pencils)
    }

    @PatchMapping("/acquired/{acquiredPencilId}/read")
    override fun markAsRead(
        @PathVariable acquiredPencilId: Long,
        @AuthenticationPrincipal(expression = "memberId") memberId: Long,
    ): ApiResponse<AcquiredDto.Read> {
        val isMarked = acquiredPencilService.markAsRead(acquiredPencilId)
        val isTotalRead = acquiredPencilService.isAllRead(memberId)
        return ApiResponse.onSuccess(AcquiredDto.Read(isMarked, isTotalRead))
    }

    @GetMapping("/acquired/is-total-read")
    override fun isAllRead(
        @AuthenticationPrincipal(expression = "memberId") memberId: Long,
    ): ApiResponse<AcquiredDto.ReadStatus> {
        val isTotalRead = acquiredPencilService.isAllRead(memberId)
        return ApiResponse.onSuccess(AcquiredDto.ReadStatus(isTotalRead))
    }
}
