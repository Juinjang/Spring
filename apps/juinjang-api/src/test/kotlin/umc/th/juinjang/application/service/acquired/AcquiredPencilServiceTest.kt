package umc.th.juinjang.application.service.acquired

import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencil
import umc.th.juinjang.domain.pencil.acquired.AcquiredType
import umc.th.juinjang.infrastructure.pencil.acquired.AcquiredPencilJpaRepository
import umc.th.juinjang.support.IntegrationTestSupport

class AcquiredPencilServiceTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var pencilService: AcquiredPencilService

    @Autowired
    private lateinit var acquiredPencilJpaRepository: AcquiredPencilJpaRepository

    @AfterEach
    fun tearDown() {
        acquiredPencilJpaRepository.deleteAllInBatch()
    }

    @Nested
    @DisplayName("getAcquiredPencilsByMember")
    inner class GetAcquiredPencilsByMember {

        @Test
        @DisplayName("멤버의 연필 히스토리 목록을 반환한다")
        fun returnsList() {
            // arrange
            val memberId = 1L
            acquiredPencilJpaRepository.save(createPencil(memberId = memberId, content = "노트 공유 보상"))
            acquiredPencilJpaRepository.save(createPencil(memberId = memberId, content = "광고 시청 보상"))

            // act
            val result = pencilService.getAcquiredPencilsByMember(memberId)

            // assert
            assertThat(result).hasSize(2)
        }

        @Test
        @DisplayName("히스토리가 없으면 빈 목록을 반환한다")
        fun returnsEmptyList() {
            // act
            val result = pencilService.getAcquiredPencilsByMember(999L)

            // assert
            assertThat(result).isEmpty()
        }
    }

    @Nested
    @DisplayName("findById")
    inner class FindById {

        @Test
        @DisplayName("ID로 연필 히스토리를 조회한다")
        fun returnsEntity() {
            // arrange
            val saved = acquiredPencilJpaRepository.save(
                createPencil(memberId = 1L, content = "노트 공유 보상"),
            )

            // act
            val result = pencilService.findById(saved.id!!)

            // assert
            assertThat(result.id).isEqualTo(saved.id)
            assertThat(result.content).isEqualTo("노트 공유 보상")
        }

        @Test
        @DisplayName("존재하지 않는 ID면 EntityNotFoundException을 던진다")
        fun throwsWhenNotFound() {
            // act & assert
            assertThatThrownBy { pencilService.findById(999L) }
                .isInstanceOf(EntityNotFoundException::class.java)
                .hasMessageContaining("999")
        }
    }

    @Nested
    @DisplayName("existsByMemberIdAndIsReadFalse")
    inner class ExistsByMemberIdAndIsReadFalse {

        @Test
        @DisplayName("안 읽은 히스토리가 있으면 true를 반환한다")
        fun returnsTrue() {
            // arrange
            val memberId = 1L
            acquiredPencilJpaRepository.save(createPencil(memberId = memberId, content = "보상"))

            // act
            val result = pencilService.existsByMemberIdAndIsReadFalse(memberId)

            // assert
            assertThat(result).isTrue()
        }

        @Test
        @DisplayName("모두 읽었으면 false를 반환한다")
        fun returnsFalse() {
            // act (데이터 없음)
            val result = pencilService.existsByMemberIdAndIsReadFalse(1L)

            // assert
            assertThat(result).isFalse()
        }
    }

    @Nested
    @DisplayName("markAsRead")
    inner class MarkAsRead {

        @Test
        @DisplayName("읽음 처리 후 true를 반환하고 DB에 반영된다")
        fun returnsTrueAndMarksRead() {
            // arrange
            val saved = acquiredPencilJpaRepository.save(
                createPencil(memberId = 1L, content = "노트 공유 보상"),
            )

            // act
            val result = pencilService.markAsRead(saved.id!!)

            // assert
            assertThat(result).isTrue()
            val reloaded = acquiredPencilJpaRepository.findById(saved.id!!).orElseThrow()
            assertThat(reloaded.isRead).isTrue()
        }
    }

    @Nested
    @DisplayName("isAllRead")
    inner class IsAllRead {

        @Test
        @DisplayName("모두 읽었으면 true를 반환한다")
        fun returnsTrue() {
            // act (데이터 없음)
            val result = pencilService.isAllRead(1L)

            // assert
            assertThat(result).isTrue()
        }

        @Test
        @DisplayName("안 읽은 것이 있으면 false를 반환한다")
        fun returnsFalse() {
            // arrange
            val memberId = 1L
            acquiredPencilJpaRepository.save(createPencil(memberId = memberId, content = "보상"))

            // act
            val result = pencilService.isAllRead(memberId)

            // assert
            assertThat(result).isFalse()
        }
    }

    private fun createPencil(memberId: Long, content: String): AcquiredPencil =
        AcquiredPencil.create(
            memberId = memberId,
            content = content,
            sharedNoteId = null,
            acquiredQuantity = 5L,
            type = AcquiredType.NOTE,
        )
}
