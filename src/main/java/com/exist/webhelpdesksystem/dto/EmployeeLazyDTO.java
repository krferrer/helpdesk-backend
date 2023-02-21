package com.exist.webhelpdesksystem.dto;


import com.exist.webhelpdesksystem.entity.Department;
import com.exist.webhelpdesksystem.entity.Employee;
import lombok.Data;

@Data
public class EmployeeLazyDTO {
    private int id;
    private String username;

    private String firstName;
    private String middleName;
    private String lastName;
    private int employeeNumber;

    private Department department;

    public EmployeeLazyDTO employeeToLazyEmployee(Employee employee){
        if(employee !=null){
            this.setId(employee.getId());
            this.setEmployeeNumber(employee.getEmployeeNumber());
            this.setUsername(employee.getUsername());
            this.setFirstName(employee.getFirstName());
            this.setMiddleName(employee.getMiddleName());
            this.setLastName(employee.getLastName());
            this.setDepartment(employee.getDepartment());
            return this;
        }
        return null;
    }
}
