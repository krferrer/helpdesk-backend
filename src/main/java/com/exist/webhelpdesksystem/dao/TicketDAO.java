package com.exist.webhelpdesksystem.dao;

import com.exist.webhelpdesksystem.entity.Ticket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface TicketDAO extends CrudRepository<Ticket,Integer> {

    @EntityGraph(attributePaths = {"watchers","assignee","watchers.role","watchers.watchTickets","watchers.watchTickets.watchers"})
    Optional<Ticket> findById(Integer integer);


     List<Ticket> findAll();

    @Query("SELECT t FROM Ticket t WHERE t.assignee.employeeNumber=:employeeId")
    List<Ticket> findByAssignee(@Param("employeeId")int employeeId);

    @Query("SELECT t from Ticket t WHERE t.assignee=null")
    List<Ticket> filterTicketsByNoAssignee();
}
