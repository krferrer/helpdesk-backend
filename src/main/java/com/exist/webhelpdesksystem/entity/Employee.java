package com.exist.webhelpdesksystem.entity;


import com.exist.webhelpdesksystem.dto.EmployeeEagerDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@NamedEntityGraph(name = "employeeWithEagerProperties",attributeNodes = {
        @NamedAttributeNode(value = "role"),
        @NamedAttributeNode(value = "watchTickets",subgraph = "watchTicketsWithWatchers")
},subgraphs = {
        @NamedSubgraph(name = "watchTicketsWithWatchers",attributeNodes = {
                @NamedAttributeNode(value="assignee",subgraph = "watchersWithRole"),
                @NamedAttributeNode(value = "watchers",subgraph = "watchersWithRole")
        }),
        @NamedSubgraph(name="watchersWithRole",attributeNodes = {
                @NamedAttributeNode(value="role")
        })
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "employee_number",columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
    private int employeeNumber;

    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="middle_name")
    private String middleName;

    @Enumerated(EnumType.STRING)
    private Department department;

    @ManyToMany
    @JoinTable(name = "employee_ticket"
            ,joinColumns = {@JoinColumn(name="employee_id")}
            ,inverseJoinColumns = {@JoinColumn(name = "ticket_id")})
    private List<Ticket> watchTickets;


    public void addWatchTicket(Ticket ticket){
        boolean isTicketDuplicate = watchTickets.stream().anyMatch((ticketElement)->{
            return ticketElement.getTicketNumber() == ticket.getTicketNumber();
        });
    }
}
