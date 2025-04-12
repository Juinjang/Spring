package umc.th.juinjang.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import umc.th.juinjang.api.pencil.controller.PencilController;
import umc.th.juinjang.api.pencil.service.AcquiredPencilService;
import umc.th.juinjang.api.pencilAccount.controller.PencilAccountController;
import umc.th.juinjang.api.pencilAccount.service.PencilAccountService;

@WebMvcTest(controllers = {
	PencilController.class,
	PencilAccountController.class
})
public abstract class ControllerTestSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected PencilAccountService pencilAccountService;

	@MockBean
	protected AcquiredPencilService acquiredPencilService;
}
