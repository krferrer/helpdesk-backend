package com.exist.webhelpdesksystem.dao;

import com.exist.webhelpdesksystem.entity.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDAO extends CrudRepository<Employee,Integer> {
    Optional<Employee> findByUsername(String username);
}
