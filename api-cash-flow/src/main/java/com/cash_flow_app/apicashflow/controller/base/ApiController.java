package com.cash_flow_app.apicashflow.controller.base;

import com.cash_flow_app.apicashflow.controller.base.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

public abstract class ApiController {

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse> handleException(Exception exception, HttpServletRequest request) {
        String errorDetail = HttpStatus.BAD_REQUEST.getReasonPhrase();
        if (exception instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) exception;
            FieldError fieldError = e.getFieldError();
            if(fieldError!=null){
                String defaultMessage = fieldError.getDefaultMessage();
                if(defaultMessage != null) errorDetail = defaultMessage;
            }
            return badRequestError(errorDetail, request.getRequestURI());

        }else if(exception instanceof HttpMessageNotReadableException){
            HttpMessageNotReadableException e = (HttpMessageNotReadableException) exception;
            String[] errorMessages;
            String fullErrorMessageStack = e.getMessage();
            if(fullErrorMessageStack!=null){
                errorMessages = fullErrorMessageStack.split(";");
            }else{
                errorMessages = null;
            }
            if(errorMessages!=null && errorMessages.length > 0){
                errorDetail = errorMessages[0];
            }
            errorDetail = errorDetail.replaceAll("java.lang.", "");
            errorDetail = errorDetail.replaceAll("\"", "`");
            return badRequestError(errorDetail, request.getRequestURI());
        }
        return badRequestError(HttpStatus.BAD_REQUEST.getReasonPhrase(), request.getRequestURI());
    }


    public static ResponseEntity<ApiResponse> okResponse(ApiResponse entity){
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    public static ResponseEntity<ApiResponse> customError(String errorMessage, HttpStatus status, String path){
        ErrorResponseApi response = new ErrorResponseApi();
        response.setError(errorMessage);
        response.setStatus(status.value());
        response.setPath(path);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ApiResponse> internalServerError(String errorMessage, String path){
        return customError(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, path);
    }

    public static ResponseEntity<ApiResponse> unprocessableEntityError(
            String errorMessage, String path){
        return customError(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY, path);
    }

    public static ResponseEntity<ApiResponse> unauthorizedError(String errorMessage, String path){
        return customError(errorMessage, HttpStatus.UNAUTHORIZED, path);
    }

    public static ResponseEntity<ApiResponse> badRequestError(String errorMessage, String path){
        return customError(errorMessage, HttpStatus.BAD_REQUEST, path);
    }

    @Data
    private static class ErrorResponseApi implements ApiResponse {
        private LocalDateTime timestamp = LocalDateTime.now();
        private Integer status;
        private String error;
        private String path;
    }
}
