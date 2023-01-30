package com.exist.webhelpdesksystem.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne
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

    @Column(name ="watch_id")
    private Integer watchId;
}
