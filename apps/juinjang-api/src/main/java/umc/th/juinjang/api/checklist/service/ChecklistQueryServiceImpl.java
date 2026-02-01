package umc.th.juinjang.api.checklist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.ChecklistHandler;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.api.checklist.service.converter.ChecklistAnswerAndReportConverter;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerResponseDTO;
import umc.th.juinjang.api.checklist.service.response.ReportResponseDTO;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.report.model.Report;
import umc.th.juinjang.domain.checklist.repository.ChecklistAnswerRepository;
import umc.th.juinjang.domain.checklist.repository.ChecklistQuestionRepository;
import umc.th.juinjang.domain.report.repository.ReportRepository;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistQueryServiceImpl implements ChecklistQueryService {

    private final ChecklistQuestionRepository checklistQuestionRepository;
    private final ChecklistAnswerRepository checklistAnswerRepository;
    private final LimjangRepository limjangRepository;
    private final ReportRepository reportRepository;

//    @Override
//    public List<ChecklistQuestionDTO.QuestionListDto> getChecklistQuestionListByVersion(int version) {
//        System.out.println("int version : " + version);
//        System.out.println("enum version : " + ChecklistQuestionVersion.find(version));
//        List<ChecklistQuestionDTO.QuestionListDto> questionListDtos = new ArrayList<>();
//        for (ChecklistQuestionCategory category : ChecklistQuestionCategory.values()) {
//            ChecklistQuestionDTO.QuestionListDto questionListDto = new ChecklistQuestionDTO.QuestionListDto();
//            questionListDtos.add(questionListDto.builder()
//                    .category(category.getValue())
//                    .questionDtos(ChecklistQuestionConverter.toChecklistQuestionListDTO(checklistQuestionRepository.findChecklistQuestionsByPurposeAndCategory(LimjangPurpose.find(version), category)))
//                    .build());
//        }
//        return questionListDtos;
//    }



    @Override
    public List<ChecklistAnswerResponseDTO.AnswerDto> getChecklistAnswerListByLimjang(Long limjangId) {
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

//    public List<ChecklistQuestionDTO.QuestionListDto> getChecklistByLimjang(Long limjangId) {
//        Limjang limjang = limjangRepository.findById(limjangId)
//                .orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
//        List<ChecklistQuestionDTO.QuestionListDto> questionListDtos = new ArrayList<>();
//        List<ChecklistAnswer> answerList = checklistAnswerRepository.findChecklistAnswerByLimjangId(limjang);
//        for (ChecklistQuestionCategory category : ChecklistQuestionCategory.values()) {
//            ChecklistQuestionDTO.QuestionListDto questionListDto = new ChecklistQuestionDTO.QuestionListDto();
//            questionListDtos.add(questionListDto.builder()
//                    .category(category.getValue())
//                    .questionDtos(ChecklistQuestionConverter.toChecklistQuestionListDTO(checklistQuestionRepository.findChecklistQuestionsByPurposeAndCategory(limjang.getPurpose(), category)))
//                    .build());
//        }
//        for (ChecklistQuestionDTO.QuestionListDto dto : questionListDtos) {
//            List<ChecklistQuestionDTO.QuestionDto> questionDtos = dto.getQuestionDtos();
//            for (ChecklistQuestionDTO.QuestionDto questionDto : questionDtos) {
//                for (ChecklistAnswer answer : answerList) {
//                    if (Objects.equals(questionDto.getQuestionId(), answer.getQuestionId().getQuestionId()))
//                        questionDto.setAnswer(answer.getAnswer());
//                }
//            }
//
//        }
//
//        return questionListDtos;
//    }

    @Override
    public ReportResponseDTO getReportByLimjangId(Long limjangId) {
        Limjang limjang = limjangRepository.findById(limjangId)
                .orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
        Report report = reportRepository.findByLimjangId(limjang)
                .orElseThrow(() -> new ChecklistHandler(ErrorStatus.REPORT_NOTFOUND_ERROR));
        return ChecklistAnswerAndReportConverter.toReportDto(report, limjang);
    }
}
