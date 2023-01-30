package com.exist.webhelpdesksystem.controller;


import com.exist.webhelpdesksystem.entity.Role;
import com.exist.webhelpdesksystem.service.RoleService;
import com.exist.webhelpdesksystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;



    @GetMapping("/role")
    public ResponseEntity<Object> getAllRoles(){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",roleService.getAllRoles());
    }
    @PostMapping("/role")
    public ResponseEntity<Object> createRole(@RequestBody Role role){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",roleService.createRole(role));
    }
}
