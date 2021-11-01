package com.task.simpleshop.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.simpleshop.exception.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<Object> handleException(Throwable throwable, WebRequest webRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (throwable instanceof NotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (throwable instanceof DataIntegrityViolationException) {
            httpStatus = HttpStatus.CONFLICT;
        }
        return handleExceptionInternal((Exception) throwable, null, httpHeaders, httpStatus, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders httpHeaders,
            HttpStatus httpStatus,
            WebRequest webRequest
    ) {
        record ErrorResponse(@JsonProperty("error_message") String errorMessage){}

        String exceptionMessage = "An error occurred: " + exception.getMessage();

        if(exception instanceof NotFoundException notFoundException) {
            exceptionMessage = notFoundException.getMessage();
        } else if (exception instanceof DataIntegrityViolationException dataIntegrityViolationException) {
            exceptionMessage = dataIntegrityViolationException.getCause().getCause().getMessage();
        }

        return ResponseEntity.status(httpStatus).body(new ErrorResponse(exceptionMessage));
    }
}
