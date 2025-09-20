package com.example.BankingBackend.Service.Exception;

import com.example.BankingBackend.Service.Exception.ErrorResponse;
import com.example.BankingBackend.Service.Exception.UserRequestNotFound;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalAccountExceptionHandlers {
    public GlobalAccountExceptionHandlers() {
        System.out.println("Global Account Exception Handler invoked!");
    }

    @ExceptionHandler(value = UserRequestNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleAccountDoesNotExistException(UserRequestNotFound ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
    @ExceptionHandler(value = AccountNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse AccountNotFoundException(AccountNotFound ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
    @ExceptionHandler(value = LoanRequestNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse LoanRequestNotFound(LoanRequestNotFound ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
    }

    // ----------------- General Exception -----------------
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleGeneralException(Exception ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }


//    @ExceptionHandler(value = AccountAlreadyExistsException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public @ResponseBody ErrorResponse handleAccountAlreadyExistsException(AccountAlreadyExistsException ex) {
//        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
//    }
}