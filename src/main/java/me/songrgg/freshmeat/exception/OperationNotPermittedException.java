package me.songrgg.freshmeat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Operation not permitted exception.
 *
 * @author songrgg
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Operation not permitted")
public class OperationNotPermittedException extends RuntimeException {
  public OperationNotPermittedException() {
    this("OPERATION_NOT_PERMITTED");
  }

  public OperationNotPermittedException(String message) {
    super(message);
  }
}
