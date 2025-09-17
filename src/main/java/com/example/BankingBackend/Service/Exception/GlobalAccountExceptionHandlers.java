package com.example.BankingBackend.Service.Exception;

import com.example.BankingBackend.Service.Exception.ErrorResponse;
import com.example.BankingBackend.Service.Exception.UserRequestNotFound;
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

//    @ExceptionHandler(value = AccountAlreadyExistsException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public @ResponseBody ErrorResponse handleAccountAlreadyExistsException(AccountAlreadyExistsException ex) {
//        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
//    }
}