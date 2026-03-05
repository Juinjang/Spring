package umc.th.juinjang.interfaces.api.pencil.acquired

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import umc.th.juinjang.api.dto.ApiResponse

@Tag(name = "얻은 연필", description = "얻은 연필(보상) API")
interface AcquiredApiSpec {

    @Operation(summary = "얻은 연필 목록", description = "사용자의 얻은 연필 목록을 불러온다")
    fun getAcquiredPencils(memberId: Long): ApiResponse<List<AcquiredDto.List>>

    @Operation(summary = "얻은 연필 읽음 처리", description = "얻은 연필 목록에서 읽음 처리를 진행한다")
    fun markAsRead(acquiredPencilId: Long, memberId: Long): ApiResponse<AcquiredDto.Read>

    @Operation(summary = "얻은 연필 전체 읽음 여부", description = "얻은 연필 목록에서 읽지 않은 항목이 존재하는 여부를 확인한다")
    fun isAllRead(memberId: Long): ApiResponse<AcquiredDto.ReadStatus>
}
