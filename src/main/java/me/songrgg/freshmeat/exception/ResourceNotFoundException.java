package me.songrgg.freshmeat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Resource not found exception.
 *
 * @author songrgg
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException() {
    this("RESOURCE_NOT_FOUND");
  }

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
