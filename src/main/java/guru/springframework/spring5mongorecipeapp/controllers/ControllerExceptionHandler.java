package guru.springframework.spring5mongorecipeapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WebExchangeBindException.class)
    public String handleNotFound(Exception exception, Model model) {
        log.error("Handling binding exception.");
        log.error("Message: " + exception.getMessage());

        model.addAttribute("exception", exception);

        return "400";
    }

}
