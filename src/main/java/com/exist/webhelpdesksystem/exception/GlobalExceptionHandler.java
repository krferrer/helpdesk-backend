package com.exist.webhelpdesksystem.exception;


import org.springframework.expression.spel.ast.Assign;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> generateNotFoundResponse(NotFoundException nfe){
        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpStatus.NOT_FOUND);
        map.put("message",nfe.getMessage());
        return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeleteException.class)
    public ResponseEntity<Object> generateDeleteException(DeleteException de){
        Map<String,Object> map = new HashMap<>();
        map.put("code", HttpStatus.FORBIDDEN);
        map.put("message",de.getMessage());
        return  new ResponseEntity<>(map,HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(AssignEmployeeException.class)
    public ResponseEntity<Object> generateAssignEmployeeException(AssignEmployeeException aex ){
        Map<String,Object> map = new HashMap<>();
        map.put("code", HttpStatus.FORBIDDEN);
        map.put("message",aex.getMessage());
        return  new ResponseEntity<>(map,HttpStatus.FORBIDDEN);
    }
}
