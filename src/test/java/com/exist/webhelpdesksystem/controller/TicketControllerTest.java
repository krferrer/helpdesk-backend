package com.exist.webhelpdesksystem.controller;

import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Ticket;
import com.exist.webhelpdesksystem.request.AssignTicketRequest;
import com.exist.webhelpdesksystem.request.AssignWatcherRequest;
import com.exist.webhelpdesksystem.request.TicketCreationRequest;
import com.exist.webhelpdesksystem.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("controller")
@Tag("ticketcontrollertest")
class TicketControllerTest {


    Ticket ticket;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TicketService ticketService;

    @BeforeEach
    void initTests(){
        ticket = new Ticket();
        ticket.setDescription("test description");
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void getAllTickets() throws Exception {
        ticket.setDescription("test description");
        when(ticketService.findAllTickets()).thenReturn(Collections.singletonList(ticket));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data[0].description",is("test description")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void getTicket() throws Exception {
        when(ticketService.findTicket(anyInt())).thenReturn(ticket);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.description",is("test description")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void getTicketsByEmployeeNumber() throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        when(ticketService.getTicketByEmployeeNumber(anyInt())).thenReturn(tickets);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/employee-number/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data[0].description",is("test description")));
        verify(ticketService).getTicketByEmployeeNumber(1);
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void createTicket() throws Exception {
        TicketCreationRequest request = new TicketCreationRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        request.setDescription("test request description");
        when(ticketService.createTicket(any(TicketCreationRequest.class))).thenReturn(ticket);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/ticket")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.description",is("test description")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void updateTicket() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        ticket.setDescription("updated ticket");
        when(ticketService.updateTicket(any(Ticket.class))).thenReturn(ticket);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/ticket")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.description",is("updated ticket")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void deleteTicket() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/ticket/1")
                .with(csrf()))
                .andExpect(status().isOk());
        verify(ticketService).deleteTicket(1);
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void assignWatchers() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AssignTicketRequest request = new AssignTicketRequest();
        Employee employee = new Employee();
        employee.setFirstName("test name");
        when(ticketService.assignWatcher(any(AssignWatcherRequest.class))).thenReturn(employee);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/ticket/assign-watchers")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName",is("test name")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void getAssignedTicket() throws Exception {
        when(ticketService.getAssignedTicket(anyInt())).thenReturn(ticket);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/assigned-ticket/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.description",is("test description")));
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void filterTicketsByNoAssignee() throws  Exception {
        when(ticketService.filterTicketsByNoAssignee()).thenReturn(Collections.singletonList(ticket));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/no-assignee-tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].description",is("test description")));
    }
}