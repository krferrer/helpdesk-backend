package com.exist.webhelpdesksystem.dto;

import com.exist.webhelpdesksystem.entity.Department;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Role;
import com.exist.webhelpdesksystem.entity.Ticket;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EmployeeEagerDTO {

    private int id;
    private String username;

    private String firstName;
    private String middleName;
    private String lastName;
    private int employeeNumber;

    private List<TicketLazyDTO> watchTickets;

    private TicketLazyDTO assignedTicket;

    private Role role;

    private Department department;

    public EmployeeEagerDTO employeeToEagerEmployee(Employee employee){
        if(employee !=null){
            this.setId(employee.getId());
            this.setEmployeeNumber(employee.getEmployeeNumber());
            this.setUsername(employee.getUsername());
            this.setFirstName(employee.getFirstName());
            this.setMiddleName(employee.getMiddleName());
            this.setLastName(employee.getLastName());
            List<TicketLazyDTO> ticketDTOs = employee.getWatchTickets().stream().map(ticket -> {
                TicketLazyDTO ticketLazyDTO = new TicketLazyDTO();
                return ticketLazyDTO.ticketToLazyDTO(ticket);
            }).collect(Collectors.toList());
            this.setWatchTickets(ticketDTOs);
            this.setRole(employee.getRole());
            this.setDepartment(employee.getDepartment());
            return this;
        }
    return null;
    }
}
