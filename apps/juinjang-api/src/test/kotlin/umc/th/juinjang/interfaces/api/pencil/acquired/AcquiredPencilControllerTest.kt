package umc.th.juinjang.interfaces.api.pencil.acquired

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import umc.th.juinjang.domain.member.model.Member
import umc.th.juinjang.domain.member.repository.MemberRepository
import umc.th.juinjang.domain.note.shared.model.SharedNote
import umc.th.juinjang.domain.note.shared.repository.SharedNoteRepository
import umc.th.juinjang.domain.pencil.acquired.AcquiredPencil
import umc.th.juinjang.domain.pencil.acquired.AcquiredType
import umc.th.juinjang.infrastructure.pencil.acquired.AcquiredPencilJpaRepository
import umc.th.juinjang.support.IntegrationTestSupport

@AutoConfigureMockMvc
class AcquiredPencilControllerTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var acquiredPencilJpaRepository: AcquiredPencilJpaRepository

    @Autowired
    private lateinit var sharedNoteRepository: SharedNoteRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var testMember: Member

    @AfterEach
    fun tearDown() {
        acquiredPencilJpaRepository.deleteAllInBatch()
        sharedNoteRepository.deleteAllInBatch()
    }

    private fun savedMember(): Member {
        if (!::testMember.isInitialized) {
            testMember = memberRepository.save(
                Member.createKakaoMember("test@test.com", 12345L, "테스터", "1.0.0"),
            )
        }
        return testMember
    }

    private fun auth(): TestingAuthenticationToken {
        val member = savedMember()
        return TestingAuthenticationToken(member, null, "ROLE_USER")
    }

    @Nested
    @DisplayName("GET /api/v2/pencil/acquired")
    inner class GetAcquiredPencils {

        @Test
        @DisplayName("얻은 연필 목록을 정상 조회한다")
        fun returnsList() {
            // arrange
            val member = savedMember()
            acquiredPencilJpaRepository.save(
                AcquiredPencil.create(member.memberId, "노트 공유 보상", null, 5L, AcquiredType.NOTE),
            )
            acquiredPencilJpaRepository.save(
                AcquiredPencil.create(member.memberId, "광고 시청 보상", null, 3L, AcquiredType.AD),
            )

            // act & assert
            mockMvc.perform(
                get("/api/v2/pencil/acquired")
                    .with(authentication(auth()))
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.length()").value(2))
        }

        @Test
        @DisplayName("데이터가 없으면 빈 배열을 반환한다")
        fun returnsEmptyList() {
            // act & assert
            mockMvc.perform(
                get("/api/v2/pencil/acquired")
                    .with(authentication(auth()))
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result").isArray)
                .andExpect(jsonPath("$.result.length()").value(0))
        }

        @Test
        @DisplayName("sharedNote가 있으면 buildingName을 함께 반환한다")
        fun returnsBuildingName() {
            // arrange
            val member = savedMember()
            val sharedNote = sharedNoteRepository.save(
                SharedNote.builder()
                    .buildingName("래미안아파트")
                    .viewCount(0L)
                    .isImageShared(false)
                    .build(),
            )

            acquiredPencilJpaRepository.save(
                AcquiredPencil.create(
                    member.memberId, "노트 공유 보상", sharedNote.sharedNoteId, 5L, AcquiredType.NOTE,
                ),
            )

            // act & assert
            mockMvc.perform(
                get("/api/v2/pencil/acquired")
                    .with(authentication(auth()))
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.result[0].buildingName").value("래미안아파트"))
                .andExpect(jsonPath("$.result[0].type").value("NOTE"))
        }
    }

    @Nested
    @DisplayName("PATCH /api/v2/pencil/acquired/{id}/read")
    inner class MarkAsRead {

        @Test
        @DisplayName("읽음 처리 후 isMarked=true를 반환한다")
        fun marksAsRead() {
            // arrange
            val member = savedMember()
            val saved = acquiredPencilJpaRepository.save(
                AcquiredPencil.create(member.memberId, "노트 공유 보상", null, 5L, AcquiredType.NOTE),
            )

            // act & assert
            mockMvc.perform(
                patch("/api/v2/pencil/acquired/${saved.id}/read")
                    .with(authentication(auth()))
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.isMarked").value(true))
        }

        @Test
        @DisplayName("마지막 항목을 읽으면 isTotalRead=true를 반환한다")
        fun returnsAllReadTrue() {
            // arrange
            val member = savedMember()
            val saved = acquiredPencilJpaRepository.save(
                AcquiredPencil.create(member.memberId, "보상", null, 5L, AcquiredType.AD),
            )

            // act & assert
            mockMvc.perform(
                patch("/api/v2/pencil/acquired/${saved.id}/read")
                    .with(authentication(auth()))
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.result.isMarked").value(true))
                .andExpect(jsonPath("$.result.isTotalRead").value(true))
        }

        @Test
        @DisplayName("읽지 않은 항목이 남아있으면 isTotalRead=false를 반환한다")
        fun returnsAllReadFalse() {
            // arrange
            val member = savedMember()
            val saved = acquiredPencilJpaRepository.save(
                AcquiredPencil.create(member.memberId, "보상1", null, 5L, AcquiredType.AD),
            )
            acquiredPencilJpaRepository.save(
                AcquiredPencil.create(member.memberId, "보상2", null, 3L, AcquiredType.NOTE),
            )

            // act & assert
            mockMvc.perform(
                patch("/api/v2/pencil/acquired/${saved.id}/read")
                    .with(authentication(auth()))
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.result.isMarked").value(true))
                .andExpect(jsonPath("$.result.isTotalRead").value(false))
        }
    }

    @Nested
    @DisplayName("GET /api/v2/pencil/acquired/is-total-read")
    inner class IsAllRead {

        @Test
        @DisplayName("안 읽은 항목이 없으면 isTotalRead=true를 반환한다")
        fun returnsTrue() {
            // act & assert
            mockMvc.perform(
                get("/api/v2/pencil/acquired/is-total-read")
                    .with(authentication(auth()))
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.result.isTotalRead").value(true))
        }

        @Test
        @DisplayName("안 읽은 항목이 있으면 isTotalRead=false를 반환한다")
        fun returnsFalse() {
            // arrange
            val member = savedMember()
            acquiredPencilJpaRepository.save(
                AcquiredPencil.create(member.memberId, "보상", null, 5L, AcquiredType.NOTE),
            )

            // act & assert
            mockMvc.perform(
                get("/api/v2/pencil/acquired/is-total-read")
                    .with(authentication(auth()))
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.result.isTotalRead").value(false))
        }
    }
}
