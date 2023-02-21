package com.exist.webhelpdesksystem.entity;


import com.exist.webhelpdesksystem.dto.TicketEagerDTO;
import com.exist.webhelpdesksystem.dto.TicketLazyDTO;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Ticket {
    @Id
    @Column(name="ticket_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketNumber;
    private String title;
    private String description;
    private Severity severity;
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assignee")
    private Employee assignee;

    @ManyToMany(mappedBy = "watchTickets")
    private Set<Employee> watchers;

}
