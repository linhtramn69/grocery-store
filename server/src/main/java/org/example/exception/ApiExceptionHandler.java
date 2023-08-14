package org.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<String> handleApiRequestException(ApiRequestException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
    }

    @ExceptionHandler(PasswordConfirmationException.class)
    public ResponseEntity<PasswordConfirmationException> handlePasswordConfirmationException(PasswordConfirmationException exception) {
        return ResponseEntity.badRequest().body(new PasswordConfirmationException(exception.getPassword2Error()));
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<PasswordException> handlePasswordException(PasswordException exception) {
        return ResponseEntity.badRequest().body(new PasswordException(exception.getPasswordError()));
    }
}
