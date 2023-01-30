package com.exist.webhelpdesksystem.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    public static ResponseEntity<Object> generateGenericResponse(HttpStatus status, String message, Object data){
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("status", status);
        map.put("data",data);
        return new ResponseEntity<>(map,status);
    }

    public static ResponseEntity<Object> generateGenericDeleteResponse(HttpStatus status,String message){
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("status", status);
        return new ResponseEntity<>(map,status);
    }
}
