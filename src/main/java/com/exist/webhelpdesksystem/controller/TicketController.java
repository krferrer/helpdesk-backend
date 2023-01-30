package com.exist.webhelpdesksystem.controller;


import com.exist.webhelpdesksystem.entity.Ticket;
import com.exist.webhelpdesksystem.request.AssignWatcherRequest;
import com.exist.webhelpdesksystem.request.TicketCreationRequest;
import com.exist.webhelpdesksystem.service.TicketService;
import com.exist.webhelpdesksystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TicketController {
    @Autowired
    private TicketService ticketService;


    @GetMapping("/ticket")
    public ResponseEntity<Object> getAllTickets(){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",ticketService.findAllTickets());
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<Object> getTicket(@PathVariable int id){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",ticketService.findTicket(id));
    }

    @GetMapping("/ticket/employee-number/{employeeNumber}")
    public ResponseEntity<Object> getTicketsByEmployeeNumber(@PathVariable int employeeNumber){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",ticketService.getTicketByEmployeeNumber(employeeNumber));
    }

    @PostMapping("/ticket")
    public ResponseEntity<Object> createTicket(@RequestBody TicketCreationRequest ticket){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Ticket created successfully",ticketService.createTicket(ticket));
    }

    @PutMapping("/ticket")
    public ResponseEntity<Object> updateTicket(@RequestBody Ticket ticket){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Ticket updated successfully",ticketService.updateTicket(ticket));
    }

    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<Object> deleteTicket(@PathVariable int id){
        ticketService.deleteTicket(id);
        return ResponseUtil.generateGenericDeleteResponse(HttpStatus.OK,"Ticket deleted successfully");
    }

    @PutMapping("/ticket/assign-watchers")
    public ResponseEntity<Object> assignWatchers(@RequestBody AssignWatcherRequest requestBody){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Successfully assigned watchers", ticketService.assignWatcher(requestBody));
    }

    @GetMapping("/ticket/assigned-ticket/{employeeNumber}")
    public ResponseEntity<Object> getAssignedTicket(@PathVariable int employeeNumber){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Success",ticketService.getAssignedTicket(employeeNumber));
    }
    @GetMapping("/ticket/no-assignee-tickets")
    public ResponseEntity<Object> filterTicketsByNoAssignee(){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Success",ticketService.filterTicketsByNoAssignee());
    }
}
