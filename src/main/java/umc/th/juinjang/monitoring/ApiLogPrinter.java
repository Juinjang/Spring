package umc.th.juinjang.monitoring;

import static umc.th.juinjang.common.LoggerProvider.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ApiLogPrinter {
  private static final Logger logger = getLogger(ApiLogPrinter.class);

  public ApiLogPrinter() {
  }

  public void print(ApiLogGenerator apiLogGenerator) {
      logger.info(apiLogGenerator.generateLog());
  }
}
