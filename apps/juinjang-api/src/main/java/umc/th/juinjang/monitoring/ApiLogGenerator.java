package umc.th.juinjang.monitoring;

import static umc.th.juinjang.common.LoggerProvider.getLogger;

import org.slf4j.Logger;
import org.slf4j.MDC;

public abstract class ApiLogGenerator {

  protected static final Logger logger = getLogger(ApiLogGenerator.class);

  public ApiLogGenerator() {
  }

  protected StringBuilder getBaseLogInfo() {
    StringBuilder logBuilder = new StringBuilder();
    logBuilder.append("[request_id] ").append(MDC.get("request_id")).append(" ");
    return logBuilder;
  }

  abstract public String generateLog();

  abstract protected String getBody(byte[] info);
}
