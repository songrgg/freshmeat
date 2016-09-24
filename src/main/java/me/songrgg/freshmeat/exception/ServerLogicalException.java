package me.songrgg.freshmeat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author songrgg
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "SERVER_LOGICAL_ERROR")
public class ServerLogicalException extends RuntimeException {
    public ServerLogicalException() {
        this("SERVER_LOGICAL_ERROR");
    }

    public ServerLogicalException(String message) {
        super(message);
    }
}
