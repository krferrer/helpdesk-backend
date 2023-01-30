package com.exist.webhelpdesksystem.controller;

import com.exist.webhelpdesksystem.entity.Department;
import com.exist.webhelpdesksystem.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class DepartmentController {


    @GetMapping("/department")
    public ResponseEntity<Object> getDepartments(){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Success", Arrays.asList(Department.values()));
    }
}
