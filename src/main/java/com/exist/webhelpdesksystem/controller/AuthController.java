package com.exist.webhelpdesksystem.controller;


import com.exist.webhelpdesksystem.request.LoginRequest;
import com.exist.webhelpdesksystem.service.AuthService;
import com.exist.webhelpdesksystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/api")
public class AuthController {


    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest){

        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",authService.login(loginRequest));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession httpSession){

        return ResponseEntity.ok().build();
    }
}
