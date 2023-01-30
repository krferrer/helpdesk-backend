package com.exist.webhelpdesksystem.controller;


import com.exist.webhelpdesksystem.entity.Severity;
import com.exist.webhelpdesksystem.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class SeverityController {

    @GetMapping("/severity")
    public ResponseEntity<Object> getSeverity(){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success", Severity.values());
    }
}
