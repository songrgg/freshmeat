package me.songrgg.freshmeat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Conflict state exception. Return HTTP code `conflict state`.
 * @author songrgg
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Conflict State")
public class ConflictStateException extends RuntimeException {
  public ConflictStateException() {
    this("CONFLICT_STATE");
  }

  public ConflictStateException(String message) {
    super(message);
  }
}
