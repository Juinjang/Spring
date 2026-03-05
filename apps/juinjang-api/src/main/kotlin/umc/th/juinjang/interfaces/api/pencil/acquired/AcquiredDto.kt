package umc.th.juinjang.interfaces.api.pencil.acquired

import umc.th.juinjang.application.facade.acquired.AcquiredPencilResult
import java.time.LocalDateTime

class AcquiredDto {

    data class List(
        val acquiredPencilId: Long,
        val content: String,
        val sharedNoteId: Long?,
        val acquiredQuantity: Long,
        val buildingName: String?,
        val isRead: Boolean,
        val type: String,
        val createdAt: LocalDateTime,
    ) {
        companion object {
            fun from(result: AcquiredPencilResult): List = List(
                acquiredPencilId = result.acquiredPencilId,
                content = result.content,
                sharedNoteId = result.sharedNoteId,
                acquiredQuantity = result.acquiredQuantity,
                buildingName = result.buildingName,
                isRead = result.isRead,
                type = result.type.name,
                createdAt = result.createdAt,
            )
        }
    }

    data class Read(
        val isMarked: Boolean,
        val isTotalRead: Boolean,
    )

    data class ReadStatus(
        val isTotalRead: Boolean,
    )
}
