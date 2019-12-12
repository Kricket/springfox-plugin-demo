package com.ineat.springfoxplugindemo.config;

import com.ineat.springfoxplugindemo.config.swagger.ExceptionErrorCode;
import com.ineat.springfoxplugindemo.dto.ErrorDTO;
import com.ineat.springfoxplugindemo.exception.APIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {
    private final boolean debug;

    public ExceptionResponseHandler(@Value("${debug:false}")boolean debug) {
        this.debug = debug;
    }

    /**
     * This will be called when a Controller method throws anything
     * @param t The exception being thrown
     * @return The response that will be returned to the user
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDTO> handleError(Throwable t) {
        final ErrorDTO errorDTO;
        final HttpStatus status;

        if (t instanceof APIException) {
            // This is a "normal" error: it's a valid return value from the API's contract
            errorDTO = new ErrorDTO(((APIException) t).getCode(), debug ? t.getMessage() : null);
            status = HttpStatus.BAD_REQUEST;
        } else {
            // This is an unexpected error: something is wrong that doesn't have anything to do
            // with the logic of the API operation
            errorDTO = new ErrorDTO(ExceptionErrorCode.UNKNOWN.name(), debug ? t.getMessage() : null);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(errorDTO);
    }

    /**
     * This will be called if the user passes, e.g. "abc" for the force (should be a number)
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().body(new ErrorDTO(
                ExceptionErrorCode.of(ex.getClass()).name(),
                ex.getMessage()
        ));
    }
}
