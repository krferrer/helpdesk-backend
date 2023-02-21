package com.exist.webhelpdesksystem.dto;

import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Role;
import lombok.Data;

@Data
public class EmployeeAuthDTO {
    private int id;
    private String username;

    private String firstName;
    private String middleName;
    private String lastName;
    private int employeeNumber;

    private Role role;

    public EmployeeAuthDTO employeeToAuthEmployee(Employee employee){
        if(employee !=null){
            this.setId(employee.getId());
            this.setEmployeeNumber(employee.getEmployeeNumber());
            this.setUsername(employee.getUsername());
            this.setFirstName(employee.getFirstName());
            this.setMiddleName(employee.getMiddleName());
            this.setLastName(employee.getLastName());
            this.setRole(employee.getRole());
            return this;
        }
        return null;
    }
}
