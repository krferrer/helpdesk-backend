package com.exist.webhelpdesksystem.dto;

import com.exist.webhelpdesksystem.entity.Severity;
import com.exist.webhelpdesksystem.entity.Status;
import com.exist.webhelpdesksystem.entity.Ticket;
import lombok.Data;


@Data
public class TicketLazyDTO {
    private int ticketNumber;
    private String title;
    private String description;
    private Severity severity;
    private Status status;

    public TicketLazyDTO ticketToLazyDTO(Ticket ticket){
        if(ticket != null){
            this.setTicketNumber(ticket.getTicketNumber());
            this.setTitle(ticket.getTitle());
            this.setStatus(ticket.getStatus());
            this.setSeverity(ticket.getSeverity());
            this.setDescription(ticket.getDescription());
            return this;
        }
            return null;
    }
}
