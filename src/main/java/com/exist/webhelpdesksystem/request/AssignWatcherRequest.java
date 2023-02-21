package com.exist.webhelpdesksystem.request;

import com.exist.webhelpdesksystem.entity.Ticket;
import lombok.Data;

import java.util.List;

@Data
public class AssignWatcherRequest {
    private int employeeId;
    private List<Integer> ticketNumbers;
}
