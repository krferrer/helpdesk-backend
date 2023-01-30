package com.exist.webhelpdesksystem.exception;

import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.dao.TicketDAO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Ticket;
import com.exist.webhelpdesksystem.service.EmployeeService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("controlleradvice")
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeDAO employeeDAO;

    @MockBean
    TicketDAO ticketDAO;

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateNotFoundResponse_employeeNotFoundTest() throws Exception {
        when(employeeDAO.findById(anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateNotFoundResponse_ticketNotFoundTest() throws Exception {
        when(ticketDAO.findById(anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void generateDeleteException() throws  Exception {
        Employee employee = new Employee();
        Ticket ticket = new Ticket();
        when(employeeDAO.findById(anyInt())).thenReturn(Optional.of(employee));
        when(ticketDAO.findByAssignee(anyInt())).thenReturn(Collections.singletonList(ticket));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}