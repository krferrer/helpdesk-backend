package com.exist.webhelpdesksystem.controller;

import com.exist.webhelpdesksystem.entity.Status;
import com.exist.webhelpdesksystem.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusController {

    @GetMapping("/status")
    public ResponseEntity<Object> getStatus(){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Success", Status.values());
    }
}
