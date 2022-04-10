package com.epam.slsa.error;

import com.epam.slsa.error.exception.EntityAlreadyExistsException;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.error.exception.EntityRoleException;
import com.epam.slsa.error.exception.PaginationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleEntityRoleException(EntityRoleException exception) {
        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiError handleEntityNotFound(EntityNotFoundException exception) {
        return ApiError
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(PaginationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handlePagination(PaginationException exception) {
        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleEntityAlreadyExists(EntityAlreadyExistsException exception) {
        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected ApiError handleConnect() {
        return ApiError
                .builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .message("SPSA service is unavailable. Check services registered in Eureka.")
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleConstraintViolation(ConstraintViolationException exception) {
        return ApiError
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(getCVESubErrors(exception))
                .build();
    }

    private List<ApiSubError> getCVESubErrors(ConstraintViolationException ex) {
        return ex
                .getConstraintViolations()
                .stream()
                .map(this::convertViolationToError)
                .collect(Collectors.toList());
    }

    private ApiValidationError convertViolationToError(ConstraintViolation<?> constraintViolation) {
        return ApiValidationError
                .builder()
                .field(constraintViolation.getPropertyPath().toString())
                .message(constraintViolation.getMessage())
                .rejectedValue(constraintViolation.getInvalidValue().toString())
                .build();
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .message("Validation error!")
                .subErrors(getMANVESubErrors(exception))
                .build();
        return buildResponseEntity(apiError);
    }

    private List<ApiSubError> getMANVESubErrors(MethodArgumentNotValidException ex) {
        return ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::convertToValidationError)
                .collect(Collectors.toList());
    }

    private ApiValidationError convertToValidationError(FieldError fieldError) {
        return ApiValidationError
                .builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(fieldError.getRejectedValue())
                .build();
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}