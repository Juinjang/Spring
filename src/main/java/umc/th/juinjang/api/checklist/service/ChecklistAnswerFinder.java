package umc.th.juinjang.api.checklist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.checklist.repository.ChecklistAnswerRepository;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;

@Component
@RequiredArgsConstructor
public class ChecklistAnswerFinder {

	private final ChecklistAnswerRepository checklistAnswerRepository;
	private final LimjangRepository limjangRepository;

	public List<ChecklistAnswerResponseDTO.AnswerDto> findByLimjangId(Long limjangId) {
		Limjang limjang = limjangRepository.findById(limjangId)
			.orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));

		List<ChecklistAnswer> answerList = checklistAnswerRepository.findChecklistAnswerByLimjangId(limjang);
		return answerList.stream()
			.map(entity -> ChecklistAnswerResponseDTO.AnswerDto.builder()
				.answerId(entity.getAnswerId())
				.questionId(entity.getQuestionId().getQuestionId())
				.limjangId(entity.getLimjangId().getLimjangId())
				.answer(entity.getAnswer())
				.answerType(entity.getQuestionId().getAnswerType())
				.build())
			.collect(Collectors.toList());
	}
}
