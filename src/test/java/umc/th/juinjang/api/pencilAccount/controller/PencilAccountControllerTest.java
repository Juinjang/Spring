package umc.th.juinjang.api.pencilAccount.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import umc.th.juinjang.api.pencilAccount.service.PencilAccountService;
import umc.th.juinjang.domain.member.model.Member;

@WebMvcTest(PencilAccountController.class)
public class PencilAccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PencilAccountService pencilAccountService;

	@DisplayName("내 연필 개수 API 요청이 정상적으로 작동하는 가?")
	@Test
	void getTotalPencilAmountByMember() throws Exception {
		// given
		Member mockMember = Member.createKakaoMember(
			"test@naver.com",
			1234568L,
			"수필씨",
			"1.0.0"
		);

		String mockToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIn0.signature";
		Authentication authentication = new TestingAuthenticationToken(mockMember, null, "ROLE_USER");
		// JWT 토큰 검증 로직 모킹

		// 서비스 메서드 모킹
		when(pencilAccountService.getTotalPencilAmountByMember(any(Member.class)))
			.thenReturn(100L);

		// when & then
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v2/pencil-account/balance")
					.with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
					.header("Authorization", mockToken)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.result.totalBalance").value(100));
	}

}
