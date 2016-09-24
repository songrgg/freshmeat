package me.songrgg.freshmeat.config;

/**
 * @author songrgg
 * @since 1.0
 */

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.DefaultCorsProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fix the condition when the browser send empty "Access-Control-Request-Headers".
 */
class NewCorsProcessor extends DefaultCorsProcessor {
  /**
   * Handle the given request.
   */
  protected boolean handleInternal(
          ServerHttpRequest request,
          ServerHttpResponse response,
          CorsConfiguration config,
          boolean preFlightRequest
  )throws IOException {

    String requestOrigin = request.getHeaders().getOrigin();
    String allowOrigin = checkOrigin(config, requestOrigin);

    HttpMethod requestMethod = getMethodToUse(request, preFlightRequest);
    List<HttpMethod> allowMethods = checkMethods(config, requestMethod);

    List<String> requestHeaders = getHeadersToUse(request, preFlightRequest);
    List<String> allowHeaders = checkHeaders(config, requestHeaders);

    // remove the allowHeaders' check for Chrome may send empty allow header.
    if (allowOrigin == null || allowMethods == null) {
      rejectRequest(response);
      return false;
    }

    HttpHeaders responseHeaders = response.getHeaders();
    responseHeaders.setAccessControlAllowOrigin(allowOrigin);
    responseHeaders.add(HttpHeaders.VARY, HttpHeaders.ORIGIN);

    if (preFlightRequest) {
      responseHeaders.setAccessControlAllowMethods(allowMethods);
    }

    if (preFlightRequest && !allowHeaders.isEmpty()) {
      responseHeaders.setAccessControlAllowHeaders(allowHeaders);
    }

    if (!CollectionUtils.isEmpty(config.getExposedHeaders())) {
      responseHeaders.setAccessControlExposeHeaders(config.getExposedHeaders());
    }

    if (Boolean.TRUE.equals(config.getAllowCredentials())) {
      responseHeaders.setAccessControlAllowCredentials(true);
    }

    if (preFlightRequest && config.getMaxAge() != null) {
      responseHeaders.setAccessControlMaxAge(config.getMaxAge());
    }

    response.flush();
    return true;
  }

  private HttpMethod getMethodToUse(ServerHttpRequest request, boolean isPreFlight) {
    return isPreFlight ? request.getHeaders().getAccessControlRequestMethod() : request.getMethod();
  }

  private List<String> getHeadersToUse(ServerHttpRequest request, boolean isPreFlight) {
    HttpHeaders headers = request.getHeaders();
    return isPreFlight ? headers.getAccessControlRequestHeaders() :
            new ArrayList<>(headers.keySet());
  }
}
