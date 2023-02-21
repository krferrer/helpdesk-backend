package com.exist.webhelpdesksystem.service;

import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.dao.TicketDAO;
import com.exist.webhelpdesksystem.dto.EmployeeEagerDTO;
import com.exist.webhelpdesksystem.dto.TicketEagerDTO;
import com.exist.webhelpdesksystem.dto.TicketLazyDTO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Ticket;
import com.exist.webhelpdesksystem.exception.EmployeeNotFoundException;
import com.exist.webhelpdesksystem.exception.TicketNotFoundException;
import com.exist.webhelpdesksystem.request.AssignWatcherRequest;
import com.exist.webhelpdesksystem.request.TicketCreationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("service")
@Tag("ticketservicetest")
class TicketServiceTest {

    @Mock
    TicketDAO ticketDAO;

    @Mock
    EmployeeDAO employeeDAO;

    @InjectMocks
    TicketService ticketService;

    Ticket ticket;

    @BeforeEach
    void initTest(){
        ticket = new Ticket();
        ticket.setDescription("test description");
    }


    @Test
    void findAllTickets() {
        when(ticketDAO.findAll()).thenReturn(Collections.singletonList(ticket));
        List<TicketLazyDTO> tickets = ticketService.findAllTickets();
        assertEquals(tickets.iterator().next().getDescription(),"test description");
    }

    @Test
    void findTicket() {
        when(ticketDAO.findById(anyInt())).thenReturn(Optional.ofNullable(ticket));
        TicketEagerDTO ticket = ticketService.findTicket(1);
        assertEquals(ticket.getDescription(),"test description");
    }
    @Test
    void findTicket_noTicketFoundTest() {
       assertThrows(TicketNotFoundException.class,()->{
          when(ticketDAO.findById(anyInt())).thenReturn(Optional.empty());
          TicketEagerDTO ticket  = ticketService.findTicket(1);
       });
    }

    @Test
    void createTicket() {
        TicketCreationRequest request = new TicketCreationRequest();
        request.setDescription("test request");
        ticket.setDescription(request.getDescription());
        when(ticketDAO.save(any(Ticket.class))).thenReturn(ticket);
        Ticket savedTicket = ticketService.createTicket(request);
        assertEquals("test request",savedTicket.getDescription());
    }

    @Test
    void getTicketByEmployeeNumber() {
        when(ticketDAO.findByAssignee(anyInt())).thenReturn(Collections.singletonList(ticket));
        List<Ticket> ticket = ticketService.getTicketByEmployeeNumber(1);
        assertEquals(ticket.get(0).getDescription(),"test description");
        verify(ticketDAO).findByAssignee(anyInt());
    }

    @Test
    void updateTicket() {
        when(ticketDAO.existsById(anyInt())).thenReturn(true);
        when(ticketDAO.save(any(Ticket.class))).thenReturn(ticket);
        Ticket updateTicket  = ticketService.updateTicket(ticket);
        assertEquals("test description",updateTicket.getDescription());
    }

    @Test
    void updateTicket_ticketNotFountTest(){
        assertThrows(TicketNotFoundException.class,()->{
           when(ticketDAO.existsById(anyInt())).thenReturn(false);
           Ticket updateTicket = ticketService.updateTicket(ticket);
        });
    }

    @Test
    void deleteTicket() {
        Employee employee = new Employee();
        ticket.setWatchers(Collections.singleton(employee));
        when(ticketDAO.findById(anyInt())).thenReturn(Optional.ofNullable(ticket));
        ticketService.deleteTicket(1);
        verify(ticketDAO).findById(1);
    }

    @Test
    void deleteTicket_ticketNotFound(){
        assertThrows(TicketNotFoundException.class,()->{
            when(ticketDAO.findById(anyInt())).thenReturn(Optional.empty());
            ticketService.deleteTicket(1);
        });
    }


    @Test
    void assignWatcher() {
        Employee employee = new Employee();
        employee.setFirstName("test name");
        AssignWatcherRequest request = new AssignWatcherRequest();
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);
        when(employeeDAO.findById(anyInt())).thenReturn(Optional.of(employee));
        EmployeeEagerDTO assignedEmployee = ticketService.assignWatcher(request);
        assertEquals("test name",assignedEmployee.getFirstName());
    }


    @Test
    void assignWatcher_employeeNotFoundTest(){
        assertThrows(EmployeeNotFoundException.class,()->{
           when(employeeDAO.findById(anyInt())).thenReturn(Optional.empty());
           AssignWatcherRequest request = new AssignWatcherRequest();
           EmployeeEagerDTO employee = ticketService.assignWatcher(request);
           verify(employeeDAO).findById(anyInt());
        });
    }

    @Test
    void assignWatcher_ticketNotFoundTest() {
        assertThrows(TicketNotFoundException.class, () -> {
            Employee employee = new Employee();
            when(employeeDAO.findById(anyInt())).thenReturn(Optional.of(employee));
            AssignWatcherRequest request = new AssignWatcherRequest();
            request.setEmployeeId(1);
            when(ticketDAO.findById(anyInt())).thenReturn(Optional.empty());
            EmployeeEagerDTO assignedWatcher = ticketService.assignWatcher(request);
            verify(employeeDAO).findById(anyInt());
        });
    }


    @Test
    void filterTicketsByNoAssignee() {
        when(ticketDAO.filterTicketsByNoAssignee()).thenReturn(Collections.singletonList(ticket));
        List<TicketLazyDTO> tickets = ticketService.filterTicketsByNoAssignee();
        assertEquals("test description",tickets.get(0).getDescription());
        verify(ticketDAO).filterTicketsByNoAssignee();
    }

    @Test
    void getAssignedTicket() {
        when(ticketDAO.findByAssignee(anyInt())).thenReturn(Collections.singletonList(ticket));
        Ticket ticket = ticketService.getAssignedTicket(1);
        assertEquals("test description", ticket.getDescription());
    }

    @Test
    void getAssignedTicket_noTicketsFound(){
        when(ticketDAO.findByAssignee(anyInt())).thenReturn(new ArrayList<>());
        assertEquals(null,ticketService.getAssignedTicket(1));
    }
}