package com.exist.webhelpdesksystem.service;


import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.dto.EmployeeAuthDTO;
import com.exist.webhelpdesksystem.dto.EmployeeEagerDTO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Role;
import com.exist.webhelpdesksystem.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Map<String,Object> login(LoginRequest loginRequest) throws BadCredentialsException{
        Authentication authObject = null;
        String basicAuthToken = "Basic " + Base64.getEncoder().encodeToString((loginRequest.getUsername() + ":" + loginRequest.getPassword()).getBytes());
        try{
            authObject=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authObject);
            Employee employee = employeeDAO.findByUsername(loginRequest.getUsername()).get();
            EmployeeEagerDTO authDTO = new EmployeeEagerDTO();
            Role role = new Role();
            role.setName(employee.getRole().getName());
            role.setId(employee.getRole().getId());
            employee.setRole(role);
            Map<String,Object> responseData = new HashMap<>();
            responseData.put("user",authDTO.employeeToEagerEmployee(employee));
            responseData.put("accessToken",basicAuthToken);
            return responseData;
        }catch(BadCredentialsException bce){
            throw new BadCredentialsException("Invalid Credentials");
        }
    }
}
