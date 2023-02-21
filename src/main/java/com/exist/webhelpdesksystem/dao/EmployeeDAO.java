package com.exist.webhelpdesksystem.dao;

import com.exist.webhelpdesksystem.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface EmployeeDAO extends CrudRepository<Employee,Integer> {
    Optional<Employee> findByUsername(String username);


    @EntityGraph(attributePaths = {"role","watchTickets"})
     Optional<Employee> findById(Integer integer);


     List<Employee> findAll();
}
