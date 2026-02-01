package umc.th.juinjang.api.member.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import umc.th.juinjang.api.member.service.MemberService;

@WebMvcTest(MemberController.class)
@WithMockUser
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MemberService memberService;

	@DisplayName("닉네임 중복 체크 API - 중복되지 않은 닉네임")
	@Test
	void checkNickname_whenNicknameDoesNotExist_thenReturnFalse() throws Exception {
		// given
		String nickname = "newNickname";
		given(memberService.isNicknameExists(nickname)).willReturn(false);

		// when & then
		mockMvc.perform(get("/api/members/nickname/exists")
				.param("nickname", nickname)
			)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result.exists").value(false));
	}

	@DisplayName("닉네임 중복 체크 API - 중복된 닉네임")
	@Test
	void checkNickname_whenNicknameExists_thenReturnTrue() throws Exception {
		// given
		String nickname = "existingNickname";
		given(memberService.isNicknameExists(nickname)).willReturn(true);

		// when & then
		mockMvc.perform(get("/api/members/nickname/exists")
				.param("nickname", nickname))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result.exists").value(true));
	}
}
