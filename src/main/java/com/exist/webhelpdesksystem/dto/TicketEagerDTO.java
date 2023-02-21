package com.exist.webhelpdesksystem.dto;

import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Severity;
import com.exist.webhelpdesksystem.entity.Status;
import com.exist.webhelpdesksystem.entity.Ticket;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TicketEagerDTO {

    private int ticketNumber;
    private String title;
    private String description;
    private Severity severity;
    private Status status;

    private List<EmployeeLazyDTO> watchers;

    private EmployeeLazyDTO assignee;

    public TicketEagerDTO ticketToEagerDTO(Ticket ticket){
        if(ticket != null){
            this.setTicketNumber(ticket.getTicketNumber());
            this.setTitle(ticket.getTitle());
            this.setStatus(ticket.getStatus());
            this.setSeverity(ticket.getSeverity());
            this.setDescription(ticket.getDescription());
            this.setWatchers(ticket.getWatchers().stream().map(employee -> {
                EmployeeLazyDTO employeeLazyDTO = new EmployeeLazyDTO();
                return employeeLazyDTO.employeeToLazyEmployee(employee);
            }).collect(Collectors.toList()));

            EmployeeLazyDTO employeeDTO = new EmployeeLazyDTO();
            this.setAssignee(employeeDTO.employeeToLazyEmployee(ticket.getAssignee()));
            return this;
        }
        return null;
    }
}
