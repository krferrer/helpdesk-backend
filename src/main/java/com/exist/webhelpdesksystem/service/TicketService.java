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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {
    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    public List<TicketLazyDTO> findAllTickets(){
        List<Ticket> tickets = ticketDAO.findAll();
        return tickets.stream().map(ticket -> {
            TicketLazyDTO ticketLazyDTO = new TicketLazyDTO();
            return ticketLazyDTO.ticketToLazyDTO(ticket);
        }).collect(Collectors.toList());
    }
    public TicketEagerDTO findTicket(int id){

        Optional<Ticket> optionalTicket = ticketDAO.findById(id);
        if(!optionalTicket.isPresent()){
            throw new TicketNotFoundException("No ticket with the id of "+ id);
        }
        System.out.println(optionalTicket.get().getWatchers().size());
        TicketEagerDTO ticketEagerDTO = new TicketEagerDTO();
        return ticketEagerDTO.ticketToEagerDTO(optionalTicket.get());
    }
    public Ticket createTicket(TicketCreationRequest ticket){
        Ticket newTicket = new Ticket();
        newTicket.setStatus(ticket.getStatus());
        newTicket.setTitle(ticket.getTitle());
        newTicket.setDescription(ticket.getDescription());
        newTicket.setSeverity(ticket.getSeverity());
        return ticketDAO.save(newTicket);
    }

    public List<Ticket> getTicketByEmployeeNumber(int employeeNumber){
        return ticketDAO.findByAssignee(employeeNumber);
    }

    public Ticket updateTicket(Ticket ticket){
        int ticketId = ticket.getTicketNumber();
        boolean isTicketExist = ticketDAO.existsById(ticketId);
        if(!isTicketExist){
            throw new TicketNotFoundException("Can not update non-existent ticket with the id of: "+ ticketId);
        }
        return ticketDAO.save(ticket);
    }

    public void deleteTicket(int ticketNumber){
        Optional<Ticket> optionalTicket = ticketDAO.findById(ticketNumber);
        if(!optionalTicket.isPresent()){
            throw new TicketNotFoundException("Can not delete non-existent ticket with the id of: "+ticketNumber);
        }
        Ticket ticket = optionalTicket.get();
        ticketDAO.deleteById(ticketNumber);
    }

    public EmployeeEagerDTO assignWatcher(AssignWatcherRequest reqBody){
        Employee employee = employeeDAO.findById(reqBody.getEmployeeId())
                .orElseThrow(()-> new EmployeeNotFoundException("Non-existent employee with the id of:" + reqBody.getEmployeeId()));
        List<Ticket> updatedTickets = new ArrayList<>();
        reqBody.getTicketNumbers().stream().forEach((ticketNumber)->{
            Ticket foundTicket = ticketDAO.findById(ticketNumber)
                    .orElseThrow(()-> new TicketNotFoundException("Non-existent ticket with the id of: "+ticketNumber));
            boolean isTicketDuplicated = updatedTickets.stream().anyMatch((ticketElement)->ticketElement.getTicketNumber() == foundTicket.getTicketNumber());
            if(!isTicketDuplicated){
                updatedTickets.add(foundTicket);
            }
        });
        employee.setWatchTickets(updatedTickets);
        EmployeeEagerDTO employeeEagerDTO = new EmployeeEagerDTO();
        employeeEagerDTO.employeeToEagerEmployee(employeeDAO.save(employee));
        return employeeEagerDTO;
    }

    public List<TicketLazyDTO> filterTicketsByNoAssignee(){
        List<Ticket> tickets = ticketDAO.filterTicketsByNoAssignee();
        List<TicketLazyDTO> lazyTickets = tickets.stream().map(ticket -> {
            TicketLazyDTO ticketDTO = new TicketLazyDTO();
            return ticketDTO.ticketToLazyDTO(ticket);
        }).collect(Collectors.toList());
        return lazyTickets;
    }

    public Ticket getAssignedTicket(int employeeNumber){
        List<Ticket> ticket = getTicketByEmployeeNumber(employeeNumber);
        if(ticket.size() == 0){
            return null;
        }
        return ticket.get(0);
    }
}
