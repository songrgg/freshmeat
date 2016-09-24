package me.songrgg.freshmeat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author songrgg
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized user")
public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException() {
        this("USER_NOT_LOGIN");
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
