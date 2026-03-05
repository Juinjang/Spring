package umc.th.juinjang.application.facade.acquired

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import umc.th.juinjang.domain.note.shared.model.SharedNote
import umc.th.juinjang.domain.note.shared.repository.SharedNoteRepository
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencil
import umc.th.juinjang.domain.pencil.acquired.AcquiredType
import umc.th.juinjang.infrastructure.pencil.acquired.AcquiredPencilJpaRepository
import umc.th.juinjang.support.IntegrationTestSupport

class AcquiredPencilFacadeTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var acquiredPencilFacade: AcquiredPencilFacade

    @Autowired
    private lateinit var acquiredPencilJpaRepository: AcquiredPencilJpaRepository

    @Autowired
    private lateinit var sharedNoteRepository: SharedNoteRepository

    @AfterEach
    fun tearDown() {
        acquiredPencilJpaRepository.deleteAllInBatch()
        sharedNoteRepository.deleteAllInBatch()
    }

    @Nested
    @DisplayName("getAcquiredPencils")
    inner class GetAcquiredPencils {

        @Test
        @DisplayName("멤버의 연필 히스토리 목록을 반환한다")
        fun returnsList() {
            // arrange
            val memberId = 1L
            acquiredPencilJpaRepository.save(createPencil(memberId = memberId, content = "노트 공유 보상"))
            acquiredPencilJpaRepository.save(createPencil(memberId = memberId, content = "광고 시청 보상"))

            // act
            val result = acquiredPencilFacade.getAcquiredPencils(memberId)

            // assert
            assertThat(result).hasSize(2)
            assertThat(result).extracting("content")
                .containsExactlyInAnyOrder("노트 공유 보상", "광고 시청 보상")
        }

        @Test
        @DisplayName("히스토리가 없으면 빈 목록을 반환한다")
        fun returnsEmptyList() {
            // act
            val result = acquiredPencilFacade.getAcquiredPencils(999L)

            // assert
            assertThat(result).isEmpty()
        }

        @Test
        @DisplayName("sharedNoteId가 있으면 buildingName을 매핑한다")
        fun mapsBuildingName() {
            // arrange
            val sharedNote = sharedNoteRepository.save(
                SharedNote.builder()
                    .buildingName("래미안아파트")
                    .viewCount(0L)
                    .isImageShared(false)
                    .build(),
            )

            val memberId = 1L
            acquiredPencilJpaRepository.save(
                AcquiredPencil.create(
                    memberId = memberId,
                    content = "노트 공유 보상",
                    sharedNoteId = sharedNote.sharedNoteId,
                    acquiredQuantity = 5L,
                    type = AcquiredType.NOTE,
                ),
            )

            // act
            val result = acquiredPencilFacade.getAcquiredPencils(memberId)

            // assert
            assertThat(result).hasSize(1)
            assertThat(result[0].buildingName).isEqualTo("래미안아파트")
            assertThat(result[0].sharedNoteId).isEqualTo(sharedNote.sharedNoteId)
        }

        @Test
        @DisplayName("sharedNoteId가 없으면 buildingName은 null이다")
        fun buildingNameNullWhenNoSharedNote() {
            // arrange
            val memberId = 1L
            acquiredPencilJpaRepository.save(createPencil(memberId = memberId, content = "광고 시청 보상"))

            // act
            val result = acquiredPencilFacade.getAcquiredPencils(memberId)

            // assert
            assertThat(result).hasSize(1)
            assertThat(result[0].buildingName).isNull()
            assertThat(result[0].sharedNoteId).isNull()
        }

        @Test
        @DisplayName("여러 sharedNote를 한 번에 조회하여 buildingName을 매핑한다")
        fun mapsBuildingNameBatch() {
            // arrange
            val note1 = sharedNoteRepository.save(
                SharedNote.builder().buildingName("래미안").viewCount(0L).isImageShared(false).build(),
            )
            val note2 = sharedNoteRepository.save(
                SharedNote.builder().buildingName("힐스테이트").viewCount(0L).isImageShared(false).build(),
            )

            val memberId = 1L
            acquiredPencilJpaRepository.save(
                AcquiredPencil.create(memberId, "보상1", note1.sharedNoteId, 5L, AcquiredType.NOTE),
            )
            acquiredPencilJpaRepository.save(
                AcquiredPencil.create(memberId, "보상2", note2.sharedNoteId, 3L, AcquiredType.NOTE),
            )
            acquiredPencilJpaRepository.save(createPencil(memberId = memberId, content = "광고 보상"))

            // act
            val result = acquiredPencilFacade.getAcquiredPencils(memberId)

            // assert
            assertThat(result).hasSize(3)
            val buildingNames = result.mapNotNull { it.buildingName }
            assertThat(buildingNames).containsExactlyInAnyOrder("래미안", "힐스테이트")
        }
    }

    private fun createPencil(memberId: Long, content: String): AcquiredPencil =
        AcquiredPencil.create(
            memberId = memberId,
            content = content,
            sharedNoteId = null,
            acquiredQuantity = 5L,
            type = AcquiredType.AD,
        )
}
