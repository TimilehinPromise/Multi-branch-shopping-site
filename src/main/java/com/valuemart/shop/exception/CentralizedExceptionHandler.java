package com.valuemart.shop.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class CentralizedExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = String.format("%s parameter is missing", exception.getParameterName());
        ApiError apiError = new ApiError(BAD_REQUEST, exception.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String error = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                .collect(Collectors.joining());
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, error, error);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        var apiError = new ApiError(BAD_REQUEST, ex.getMessage(), ex.getMessage());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler({ValueMartException.class})
    public ResponseEntity<Object> handleValuePlusException(ValueMartException ex) {
        var apiError = new ApiError(ex.getHttpStatus(), ex.getMessage(), ex.getMessage());
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(DeliveryAreaNotFoundException.class)
    public ResponseEntity<Object> handleDeliveryAreaNotFoundException(DeliveryAreaNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleNotDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, ex.getMessage(), ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private static class ApiError {
        private HttpStatus status;
        private String message;
        private List<String> errors;

        public ApiError(HttpStatus status, String message, List<String> errors) {
            this.status = status;
            this.message = message;
            this.errors = errors;
        }

        public ApiError(HttpStatus status, String message, String error) {
            this.status = status;
            this.message = message;
            this.errors = singletonList(error);
        }

        public HttpStatus getStatus() {
            return status;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }
    }
}
