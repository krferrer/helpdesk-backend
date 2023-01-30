package com.exist.webhelpdesksystem.service;


import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private AuthenticationManager authenticationManager;
    public Map<String,Object> login(LoginRequest loginRequest) throws BadCredentialsException{
        Authentication authObject = null;
        String basicAuthToken = "Basic " + Base64.getEncoder().encodeToString((loginRequest.getUsername() + ":" + loginRequest.getPassword()).getBytes());
        try{
            authObject=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authObject);
            Optional<Employee> optionalEmployee = employeeDAO.findByUsername(loginRequest.getUsername());
            Map<String,Object> responseData = new HashMap<>();
            responseData.put("user",optionalEmployee.get());
            responseData.put("accessToken",basicAuthToken);
            return responseData;
        }catch(BadCredentialsException bce){
            throw new BadCredentialsException("Invalid Credentials");
        }

    }
}
