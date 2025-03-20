package umc.th.juinjang.api.limjang.service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import umc.th.juinjang.domain.limjang.repository.LimjangRepository;

@Component
@RequiredArgsConstructor
public class LimjangSchedulerService {

  private final LimjangRepository limjangRepository;

  @Transactional
  // @Scheduled(fixedRate = 60000) // 1분 간격으로 실행 - test용
//  @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000) // 일주일 간격으로 실행 <- 나중에 출시하면 이걸로
  @Scheduled(fixedRate = 31557600000L) // 일년 간격으로 실행(임시)
  public void cleanUpData() {
    // 삭제 필드 true 된지 1달된거 삭제
    LocalDateTime deletionCycle = LocalDateTime.now().minusMonths(1);

    DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    System.out.println("스케줄러 테스트  " + LocalDateTime.now().format(dtf));

    try {
      limjangRepository.hardDelete(deletionCycle);
    }catch (Exception e) {
      System.out.println("hardDelete 중 에러발생함..");
    }
  }

}
