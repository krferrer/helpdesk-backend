package com.exist.webhelpdesksystem.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

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

    @OneToOne
    @JoinColumn(name="assignee")
    private Employee assignee;

    @OneToMany(cascade = {})
    @JoinColumn(name = "watch_id",referencedColumnName = "ticket_number")
    private List<Employee> watchers;
}
