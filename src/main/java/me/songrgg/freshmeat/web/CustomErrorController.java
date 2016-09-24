package me.songrgg.freshmeat.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.NestedServletException;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Custom error controller.
 * @author songrgg
 */
@RestController
public class CustomErrorController extends BasicErrorController {

  private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

  public CustomErrorController() {
    super(new CustomErrorAttributes(CustomErrorAttributes.ENVIRONMENT.PRODUCTION), new ErrorProperties() {
      @Override
      public IncludeStacktrace getIncludeStacktrace() {
        return IncludeStacktrace.ON_TRACE_PARAM;
      }
    });
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  protected HttpStatus getStatus(HttpServletRequest request) {

    Throwable exception = getError(request);
    if (exception != null && exception instanceof NestedServletException) {
      exception = ((NestedServletException) exception).getRootCause();

      if (exception instanceof HttpClientErrorException) {
        return ((HttpClientErrorException) exception).getStatusCode();
      }
    }

    return super.getStatus(request);
  }

  @RequestMapping
  @ResponseBody
  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    // log exception for 5xx
    if (getStatus(request).is5xxServerError()) {
      RequestAttributes requestAttributes = new ServletRequestAttributes(request);
      Throwable error = (new CustomErrorAttributes(CustomErrorAttributes.ENVIRONMENT.PRODUCTION)).getError(requestAttributes);
      if (error != null) {
        logger.error(error.getMessage());
      }
    }
    return super.error(request);
  }

  private Throwable getError(HttpServletRequest request) {
    return getAttribute(request, "javax.servlet.error.exception");
  }

  @SuppressWarnings("unchecked")
  private <T> T getAttribute(HttpServletRequest requestAttributes, String name) {
    return (T) requestAttributes.getAttribute(name);
  }

}
