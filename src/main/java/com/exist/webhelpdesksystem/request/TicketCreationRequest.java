package com.exist.webhelpdesksystem.request;

import com.exist.webhelpdesksystem.entity.Severity;
import com.exist.webhelpdesksystem.entity.Status;
import lombok.Data;


@Data
public class TicketCreationRequest {

        private String title;
        private String description;
        private Severity severity;
        private Status status;

}
