package umc.th.juinjang.domain.pencil.acquired

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class AcquiredPencilTest {

    @Test
    @DisplayName("얻은 연필 히스토리가 정상적으로 만들어진다.")
    fun createPencil() {
        // given
        val memberId = 1L
        val content = "노트 공유 보상"
        val sharedNoteId = 100L
        val acquiredQuantity = 5L
        val type = AcquiredType.NOTE

        // when
        val pencil = AcquiredPencil.create(
            memberId = memberId,
            content = content,
            sharedNoteId = sharedNoteId,
            acquiredQuantity = acquiredQuantity,
            type = type,
        )

        // then
        assertThat(pencil.refMemberId).isEqualTo(memberId)
        assertThat(pencil.content).isEqualTo(content)
        assertThat(pencil.sharedNoteId).isEqualTo(sharedNoteId)
        assertThat(pencil.acquiredQuantity).isEqualTo(acquiredQuantity)
        assertThat(pencil.isRead).isFalse()
        assertThat(pencil.type).isEqualTo(AcquiredType.NOTE)
    }

    @Test
    @DisplayName("sharedNoteId 없이도 얻은 연필 히스토리가 만들어진다.")
    fun createPencilWithoutSharedNote() {
        // when
        val pencil = AcquiredPencil.create(
            memberId = 1L,
            content = "광고 시청 보상",
            sharedNoteId = null,
            acquiredQuantity = 3L,
            type = AcquiredType.AD,
        )

        // then
        assertThat(pencil.sharedNoteId).isNull()
        assertThat(pencil.type).isEqualTo(AcquiredType.AD)
    }

    @Test
    @DisplayName("얻은 연필 히스토리가 읽음 처리가 정상적으로 된다.")
    fun markAsReadIsSuccess() {
        // given
        val pencil = AcquiredPencil.create(
            memberId = 1L,
            content = "노트 공유 보상",
            sharedNoteId = null,
            acquiredQuantity = 5L,
            type = AcquiredType.NOTE,
        )

        // when
        pencil.markAsRead()

        // then
        assertThat(pencil.isRead).isTrue()
    }

    @Test
    @DisplayName("지정된 날짜로 얻은 연필 히스토리가 만들어진다.")
    fun createPencilWithDate() {
        // given
        val targetDate = LocalDateTime.of(2025, 1, 15, 10, 30)

        // when
        val pencil = AcquiredPencil.createWithDate(
            memberId = 1L,
            content = "과거 데이터 마이그레이션",
            sharedNoteId = null,
            acquiredQuantity = 1L,
            type = AcquiredType.VIEWCOUNT,
            createdAt = targetDate,
        )

        // then
        assertThat(pencil.createdAt).isEqualTo(targetDate)
        assertThat(pencil.isRead).isFalse()
    }
}
