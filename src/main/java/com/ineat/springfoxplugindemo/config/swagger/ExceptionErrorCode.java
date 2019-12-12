package com.ineat.springfoxplugindemo.config.swagger;

import com.ineat.springfoxplugindemo.exception.IllegalAngleException;
import com.ineat.springfoxplugindemo.exception.InsufficientForceException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

public enum ExceptionErrorCode {
    ILLEGAL_ANGLE(IllegalAngleException.class),
    INSUFFICIENT_FORCE(InsufficientForceException.class),
    INVALID_ARGUMENT(MethodArgumentNotValidException.class),

    UNKNOWN(Throwable.class);

    private final Class<? extends Throwable> exception;

    ExceptionErrorCode(Class<? extends Throwable> exc) {
        this.exception = exc;
    }

    /**
     * Get the enum constant corresponding to the given Exception. {@link ExceptionErrorCode#UNKNOWN}
     * is returned if none matches.
     */
    @NotNull
    public static ExceptionErrorCode of(Class<?> exc) {
        return Arrays.stream(values())
                .filter(c -> c.exception.equals(exc))
                .findAny()
                .orElse(UNKNOWN);
    }
}
