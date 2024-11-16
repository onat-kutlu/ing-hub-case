package com.ing.inghub.config;

import com.ing.inghub.constants.errorcodes.ErrorCodes;
import com.ing.inghub.dto.base.BaseResponse;
import com.ing.inghub.dto.base.ServiceResult;
import com.ing.inghub.exception.IngException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

    //ing exception handler
    @ExceptionHandler(IngException.class)
    public ResponseEntity<Object> handleAssignmentException(final IngException ex) {

        return ResponseEntity.internalServerError()
                .body(new BaseResponse(mapToServiceResult(ex)));
    }

    //request body validation exception handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(new BaseResponse(getBadRequestResult(errors.toString())));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnhandledExceptions(final Exception ex,
                                                            final WebRequest request) {

        return ResponseEntity.internalServerError().body(new BaseResponse(mapToServiceResult(ex)));
    }


    private ServiceResult mapToServiceResult(Exception exception) {
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setCode(ErrorCodes.BAD_REQUEST_ERROR_CODE);
        serviceResult.setMessage(exception.getMessage());
        serviceResult.setSuccess(false);
        return serviceResult;
    }

    private ServiceResult mapToServiceResult(IngException exception) {
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setCode(exception.getCode());
        serviceResult.setMessage(exception.getMessage());
        serviceResult.setSuccess(false);
        return serviceResult;
    }

    private ServiceResult getBadRequestResult(String message) {
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setCode(ErrorCodes.BAD_REQUEST_ERROR_CODE);
        serviceResult.setMessage(message);
        serviceResult.setSuccess(false);
        return serviceResult;
    }

}
