package com.epam.spsa.error;

import com.epam.spsa.error.exception.*;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
@Slf4j
public class ApiExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    @Value("${jwt.token.incorrect}")
    private String incorrectTokenExceptionMessage;

    @ExceptionHandler(DurationDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleDurationDate(DurationDateException exception) {
        log.info("ExceptionHandler for DurationDateException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(EntityRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleEntityRole(EntityRoleException exception) {
        log.info("ExceptionHandler for EntityRoleException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ApiError handleAccessDenied(AccessDeniedException exception) {
        log.info("ExceptionHandler for AccessDeniedException");

        return ApiError
                .builder()
                .status(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleBadCredentials(BadCredentialsException exception) {
        log.info("ExceptionHandler for BadCredentialsException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleIncorrectPassword(IncorrectPasswordException exception) {
        log.info("ExceptionHandler for IncorrectPasswordException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleJwt() {
        log.info("ExceptionHandler for JwtException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(incorrectTokenExceptionMessage)
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(PhotoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handlePhoto(PhotoException exception) {
        log.info("ExceptionHandler for PhotoException");

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
        log.info("ExceptionHandler for EntityNotFoundException");

        return ApiError
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(EntityNotFullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleEntityNotFull(EntityNotFullException exception) {
        log.info("ExceptionHandler for EntityNotFullException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(PaginationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handlePagination(PaginationException exception) {
        log.info("ExceptionHandler for PaginationException");

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
        log.info("ExceptionHandler for EntityAlreadyExistsException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(SameParticipantException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleSameParticipant(SameParticipantException exception) {
        log.info("ExceptionHandler for SameParticipantException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(UserNotParticipantException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleUserNotParticipant(UserNotParticipantException exception) {
        log.info("ExceptionHandler for UserNotParticipantException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(UserNotSenderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleUserNotSender(UserNotSenderException exception) {
        log.info("ExceptionHandler for UserNotSenderException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(NotUniqueValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleNotUniqueValue(NotUniqueValueException exception) {
        log.info("ExceptionHandler for NotUniqueValueException");

        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(WrongDurationPatternException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleWrongDurationPattern(WrongDurationPatternException exception) {
        log.info("ExceptionHandler for WrongDurationPatternException");

        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected ApiError handleConnect() {
        log.info("ExceptionHandler for ConnectException");

        return ApiError
                .builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .message("SLSA or Mail service is unavailable. Check services registered in Eureka.")
                .timestamp(LocalDateTime.now())
                .subErrors(new ArrayList<>())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleConstraintViolation(ConstraintViolationException exception) {
        log.info("ExceptionHandler for ConstraintViolationException");

        return ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .subErrors(getCVESubErrors(exception))
                .build();
    }

    public List<ApiSubError> getCVESubErrors(ConstraintViolationException ex) {
        log.info("Getting List of ApiSubErrors of ConstraintViolationException");

        return ex
                .getConstraintViolations()
                .stream()
                .map(this::convertViolationToError)
                .collect(Collectors.toList());
    }

    public ApiValidationError convertViolationToError(ConstraintViolation<?> constraintViolation) {
        log.info("Converting ConstraintViolation to ApiValidationError");
        log.info("Violation message: {}", constraintViolation.getMessage());
        log.info("Field with violation: {}", constraintViolation.getPropertyPath().toString());
        log.info("Rejected value: {}", constraintViolation.getInvalidValue());

        return ApiValidationError
                .builder()
                .field(constraintViolation.getPropertyPath().toString())
                .message(constraintViolation.getMessage())
                //ToDo fix trouble with MultipartFile
                .rejectedValue(constraintViolation.getInvalidValue().toString())
                .build();
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.info("Handling MethodArgumentNotValidException");

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
        log.info("Getting List of ApiSubErrors of MethodArgumentNotValidException");

        return ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::convertToValidationError)
                .collect(Collectors.toList());
    }

    private ApiValidationError convertToValidationError(FieldError fieldError) {
        log.info("Converting FieldError to ApiValidationError");
        log.info("Violation message: {}", fieldError.getDefaultMessage());
        log.info("Field with violation: {}", fieldError.getField());
        log.info("Rejected value: {}", fieldError.getRejectedValue());

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