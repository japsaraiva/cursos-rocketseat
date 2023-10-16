package br.com.japsaraiva.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice                               //Notação do Spring que define classes globais ao tratar exceções, 
public class ExceptionHandlerController {       //toda exceção que acontecer irá passar por esse controlador a não ser que esteja definida como abaixo

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage()); //getMessage, pega a mensagem definida na exceção em TaskModel
    }
}
