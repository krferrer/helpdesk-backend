package com.exist.webhelpdesksystem.service;

import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.dao.TicketDAO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Ticket;
import com.exist.webhelpdesksystem.exception.EmployeeNotFoundException;
import com.exist.webhelpdesksystem.exception.TicketNotFoundException;
import com.exist.webhelpdesksystem.request.AssignWatcherRequest;
import com.exist.webhelpdesksystem.request.TicketCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private EmployeeDAO employeeDAO;


    public Iterable<Ticket> findAllTickets(){
        return ticketDAO.findAll();
    }
    public Ticket findTicket(int id){
        Optional<Ticket> ticket = ticketDAO.findById(id);
        if(!ticket.isPresent()){
            throw new TicketNotFoundException("No ticket with the id of "+ id);
        }
        return ticket.get();
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
        ticket.getWatchers().stream().forEach(employee -> {
            employee.setWatchId(null);
            employeeDAO.save(employee);
        });
        ticketDAO.deleteById(ticketNumber);
    }

    public Employee assignWatcher(AssignWatcherRequest reqBody){
        Optional<Employee> optionalEmployee = employeeDAO.findById(reqBody.getEmployeeId());
        if(!optionalEmployee.isPresent()){
            throw new EmployeeNotFoundException("Employee not found");
        }
        Employee employee = optionalEmployee.get();
        if(reqBody.getTicketNumber() == null){
            employee.setWatchId(null);
            return employeeDAO.save(employee);
        }
        Optional<Ticket> optionalTicket = ticketDAO.findById(reqBody.getTicketNumber());
        if(!optionalTicket.isPresent()){
            throw new TicketNotFoundException("Ticket not found");
        }
        Ticket ticket = optionalTicket.get();
        employee.setWatchId(ticket.getTicketNumber());
        return employeeDAO.save(employee);
    }

    public List<Ticket> filterTicketsByNoAssignee(){
        return ticketDAO.filterTicketsByNoAssignee();
    }

    public Ticket getAssignedTicket(int employeeNumber){

        List<Ticket> ticket = getTicketByEmployeeNumber(employeeNumber);
        if(ticket.size() == 0){
            return null;
        }
        return ticket.get(0);
    }
}
