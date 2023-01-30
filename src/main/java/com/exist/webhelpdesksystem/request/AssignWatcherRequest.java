package com.exist.webhelpdesksystem.request;

import lombok.Data;

import java.util.List;

@Data
public class AssignWatcherRequest {
    private int employeeId;
    private Integer ticketNumber;
}
