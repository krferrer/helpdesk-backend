package com.exist.webhelpdesksystem.request;


import lombok.Data;

@Data
public class AssignTicketRequest {
    private Integer employeeId;
    private Integer ticketId;
}
