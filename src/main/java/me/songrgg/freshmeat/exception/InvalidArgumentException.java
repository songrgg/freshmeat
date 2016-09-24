package me.songrgg.freshmeat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Invalid argument exception. Return HTTP code 400.
 *
 * @author songrgg
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid argument")
public class InvalidArgumentException extends RuntimeException {
  public InvalidArgumentException() {
    this("INVALID_ARGUMENT");
  }

  public InvalidArgumentException(String message) {
    super(message);
  }
}
