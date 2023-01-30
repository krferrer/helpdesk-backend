package com.exist.webhelpdesksystem.dao;

import com.exist.webhelpdesksystem.entity.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketDAO extends CrudRepository<Ticket,Integer> {
    @Query("SELECT t FROM Ticket t WHERE t.assignee.employeeNumber=:employeeId")
    List<Ticket> findByAssignee(@Param("employeeId")int employeeId);

    @Query("SELECT t from Ticket t WHERE t.assignee=null")
    List<Ticket> filterTicketsByNoAssignee();
}
