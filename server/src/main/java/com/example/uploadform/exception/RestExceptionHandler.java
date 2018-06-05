package com.example.uploadform.exception;

import com.example.uploadform.api.ErrorResponse;
import com.example.uploadform.exception.DataAlreadyExistsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ErrorResponse(BAD_REQUEST, "Malformed JSON request", ex));
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    protected ResponseEntity<Object> handleDuplicateFile(DataAlreadyExistsException ex) {
        ErrorResponse ErrorResponse = new ErrorResponse(BAD_REQUEST, "Duplicate file upload", ex);
        return buildResponseEntity(ErrorResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<Object> handleTooLargeFile(MaxUploadSizeExceededException ex) {
        ErrorResponse ErrorResponse = new ErrorResponse(BAD_REQUEST, "File too large", ex);
        return buildResponseEntity(ErrorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> defaultErrorHandler(HttpServletRequest req, Exception e)
            throws Exception {
        logger.error("Exception", e);
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ErrorResponse ErrorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR, e);
        return buildResponseEntity(ErrorResponse);
    }


    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }


}