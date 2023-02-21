package com.exist.webhelpdesksystem.config;


import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class HelpdeskAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String requestUsername = authentication.getName();
        String requestPassword = authentication.getCredentials().toString();
        Optional<Employee> optionalEmployee = employeeDAO.findByUsername(requestUsername);
        if(!optionalEmployee.isPresent()){
            throw new BadCredentialsException("Invalid credentials");
        }else{
            Employee employee = optionalEmployee.get();
         if(passwordEncoder.matches(requestPassword,employee.getPassword()))  {
             List<GrantedAuthority> roles = new ArrayList<>();
             roles.add(new SimpleGrantedAuthority(employee.getRole().getName()));
            return new UsernamePasswordAuthenticationToken(requestUsername,requestPassword,roles);
         }else{
             throw new BadCredentialsException("Invalid Credentials!");
         }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
