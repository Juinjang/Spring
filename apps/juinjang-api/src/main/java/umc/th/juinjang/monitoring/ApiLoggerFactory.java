package umc.th.juinjang.monitoring;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class ApiLoggerFactory {

  public void createRequestLogger(ContentCachingRequestWrapper request) {
    getApiLogPrinter().print(new ApiLogRequestLogGenerator(request));
  }

  public void createResponseLogger(ContentCachingResponseWrapper response) {
    getApiLogPrinter().print(new ApiLogResponseGenerator(response));
  }

  private ApiLogPrinter getApiLogPrinter() {
    return new ApiLogPrinter();
  }
}
