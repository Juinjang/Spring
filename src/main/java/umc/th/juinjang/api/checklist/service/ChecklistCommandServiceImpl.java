package umc.th.juinjang.api.checklist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.common.exception.handler.ChecklistHandler;
import umc.th.juinjang.common.exception.handler.LimjangHandler;
import umc.th.juinjang.api.checklist.service.converter.ChecklistAnswerAndReportConverter;
import umc.th.juinjang.api.checklist.service.response.ChecklistAnswerAndReportResponseDTO;
import umc.th.juinjang.api.checklist.controller.request.ChecklistAnswerRequestDTO;
import umc.th.juinjang.domain.checklist.model.ChecklistAnswer;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionShort;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.report.model.Report;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionCategory;
import umc.th.juinjang.domain.checklist.model.ChecklistQuestionType;
import umc.th.juinjang.domain.checklist.repository.ChecklistAnswerRepository;
import umc.th.juinjang.domain.checklist.repository.ChecklistQuestionRepository;
import umc.th.juinjang.domain.report.repository.ReportRepository;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChecklistCommandServiceImpl implements ChecklistCommandService{
    private final ChecklistAnswerRepository checklistAnswerRepository;
    private final ChecklistQuestionRepository checklistQuestionRepository;
    private final LimjangRepository limjangRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public ChecklistAnswerAndReportResponseDTO saveChecklistAnswerList(Long limjangId, List<ChecklistAnswerRequestDTO.AnswerDto> answerDtoList) {
        Limjang limjang = limjangRepository.findById(limjangId)
                .orElseThrow(() -> new LimjangHandler(ErrorStatus.LIMJANG_NOTFOUND_ERROR));
        if (!checklistAnswerRepository.findChecklistAnswerByLimjangId(limjang).isEmpty()) {
            checklistAnswerRepository.deleteAllByLimjangId(limjang);
            checklistAnswerRepository.flush();
        }

        List<ChecklistAnswer> answerList = createAnswerList(limjang, answerDtoList);
        answerList = checklistAnswerRepository.saveAll(answerList);

        //ChecklistQuestion의 Category로 그룹을 지어줌
        //categorizedAnswers는 ChecklistQuestionCategory를 키로, 해당 카테고리에 해당하는 ChecklistAnswer 리스트를 값으로 가지는 Map
                Map<ChecklistQuestionCategory, List<ChecklistAnswer>> categorizedAnswers = answerList.stream()
                        .collect(Collectors.groupingBy(answer -> answer.getQuestionId().getCategory()));

        Report report = findOrCreateReport(limjang);
        reportRepository.save(calculateAndSetCategoryRates(report, categorizedAnswers));
        System.out.println("****" + report.getIndoorRate().toString() + " " + report.getPublicSpaceRate().toString() + " "+ report.getLocationConditionsRate().toString());
        System.out.println(report.getIndoorKeyword() + " " + report.getPublicSpaceKeyword() + " "+ report.getLocationConditionsKeyword());

        return ChecklistAnswerAndReportConverter.toDto(answerList, report, limjang);
    }

    private List<ChecklistAnswer> createAnswerList(Limjang limjang, List<ChecklistAnswerRequestDTO.AnswerDto> answerDtoList) {
        return answerDtoList.stream()
                .map(dto -> {
                    ChecklistQuestionShort question = checklistQuestionRepository.findById(dto.getQuestionId())
                            .orElseThrow(() -> new ChecklistHandler(ErrorStatus.CHECKLIST_NOTFOUND_ERROR));

                    return ChecklistAnswer.builder()
                            .questionId(question)
                            .limjangId(limjang)
                            .answer(dto.getAnswer())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private Report findOrCreateReport(Limjang limjang) {
        return reportRepository.findByLimjangId(limjang)
                .orElseGet(() -> Report.builder()
                        .limjangId(limjang)
                        .build());
    }

    private Report calculateAndSetCategoryRates(Report report, Map<ChecklistQuestionCategory, List<ChecklistAnswer>> categorizedAnswers) {
        float totalRate = 0F;

        //데드라인 : {answer 모음들}, 입지여건 : {answer 모음들}, 공용공간 ~~

        ChecklistQuestionCategory[] categories = ChecklistQuestionCategory.values();
        int categoryCount = categories.length;
        for (ChecklistQuestionCategory category : categories) {
            if (category == ChecklistQuestionCategory.DEADLINE) {
                categoryCount -= 1;
                continue;
            }
            List<ChecklistAnswer> answers = categorizedAnswers.get(category);
            Float categoryRate = calculateAverage(answers);

            if (categoryRate == null) {
                categoryRate = 0F;
            }
            String keyword = setRandomKeyword(categoryRate);

            System.out.println(categoryRate);
            System.out.println(keyword);
            if (categoryRate == 0f) {
                System.out.println("현재 category : " + category + " 하나 빼짐!");
                categoryCount -= 1;
            }
            switch (category) {
                case INDOOR:
                    report.setIndoorRate(categoryRate);
                    report.setIndoorKeyword(keyword);
                    System.out.println("indoor");
                    break;
                case PUBLIC_SPACE:
                    report.setPublicSpaceRate(categoryRate);
                    report.setPublicSpaceKeyword(keyword);
                    System.out.println("public space");
                    break;
                case LOCATION_CONDITION:
                    report.setLocationConditionsRate(categoryRate);
                    report.setLocationConditionsKeyword(keyword);
                    System.out.println("location condition");
                    break;

            }

            totalRate += categoryRate;
        }
        //System.out.println("####" + report.getIndoorRate().toString() + " " + report.getPublicSpaceRate().toString() + " "+ report.getLocationConditionsRate().toString());
        //System.out.println(report.getIndoorKeyword() + " " + report.getPublicSpaceKeyword() + " "+ report.getLocationConditionsKeyword());
        System.out.println("totalRate : " + totalRate + " categoryCount : " + categoryCount);
        if (categoryCount <= 0) {
            report.setTotalRate(totalRate);
        }
        else {
            report.setTotalRate(totalRate / categoryCount);
        }
        return report;
    }

    private Float calculateAverage(List<ChecklistAnswer> answers) {
        if (answers == null || answers.isEmpty()) {
            return 0f;
        }
        Float total = 0f;
        int count = 0;
        for (ChecklistAnswer answer : answers) {
            if (answer.getQuestionId().getAnswerType() == ChecklistQuestionType.SCORE) {
                System.out.println("questionId : " + answer.getQuestionId().getQuestionId() + " answerType : " + answer.getQuestionId().getAnswerType());
                total += Float.parseFloat(answer.getAnswer());
                count++;
            }
        }
        if (count == 0) {
            return 0f;
        }
        return total / count;
    }

    public String setRandomKeyword(Float rate){

        String[] oneToTwo = { "불안한" ,"불안정한" ,"불쾌한"};
        String[] twoToThree = {"평균적인", "보통의", "나쁘지 않은"};
        String[] threeToFour = {"좋은", "좋은 편인", "훌륭한", "쾌적한"};
        String[] fourToFive = {"최상의", "최고의", "상당히 좋은", "상당히 쾌적한"};
        String defaultKeyword = "아직 미평가된";

        Random random = new Random();

        if (rate >= 1 && rate < 2) {
            return oneToTwo[random.nextInt(oneToTwo.length)];
        } else if (rate >= 2 && rate < 3) {
            return twoToThree[random.nextInt(twoToThree.length)];
        } else if (rate >= 3 && rate < 4) {
            return threeToFour[random.nextInt(threeToFour.length)];
        } else if (rate >= 4 && rate <= 5) {
            return fourToFive[random.nextInt(fourToFive.length)];
        }

        return defaultKeyword;

    }
}
