package com.exist.webhelpdesksystem.service;

import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.dao.TicketDAO;
import com.exist.webhelpdesksystem.dto.EmployeeEagerDTO;
import com.exist.webhelpdesksystem.dto.EmployeeLazyDTO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Ticket;
import com.exist.webhelpdesksystem.exception.EmployeeDeleteException;
import com.exist.webhelpdesksystem.exception.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
@Tag("employeeservicetest")
class EmployeeServiceTest {

    @Mock
    EmployeeDAO employeeDAO;

    @Mock
    TicketDAO ticketDAO;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    EmployeeService employeeService;

    Employee employee;

    @BeforeEach
    void initTests(){
         employee = new Employee();
    }

    @Test
    void createEmployee() {
        employee.setFirstName("Test name");
        employee.setPassword("testpassword");
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);
        Employee savedEmployee = employeeService.createEmployee(employee);
        verify(employeeDAO).save(any(Employee.class));
        assertEquals(savedEmployee.getFirstName(),"Test name");
    }

    @Test
    void findAllEmployee() {
        employee.setFirstName("TestName");
        when(employeeDAO.findAll()).thenReturn(Collections.singletonList(employee));
        List<EmployeeLazyDTO> employeeList = employeeService.findAllEmployee();
        verify(employeeDAO).findAll();
        assertEquals(employeeList.iterator().next().getFirstName(),"TestName");
    }

    @Test
    void findEmployee() {
        employee.setFirstName("Test name by id");
        when(employeeDAO.findById(anyInt())).thenReturn(Optional.ofNullable(employee));
        EmployeeEagerDTO foundEmployee = employeeService.findEmployee(1);
        verify(employeeDAO).findById(anyInt());
        assertEquals(foundEmployee.getFirstName(),"Test name by id");
    }

    @Test
    void findEmployeeNotFoundTest(){
        assertThrows(EmployeeNotFoundException.class,()->{
            when(employeeDAO.findById(2)).thenReturn(Optional.empty());
            EmployeeEagerDTO employee = employeeService.findEmployee(2);
            verify(employeeDAO).findById(2);
        });
    }

    @Test
    void updateEmployee() {
        employee.setId(1);
        when(employeeDAO.findById(1)).thenReturn(Optional.ofNullable(employee));
        when(employeeDAO.save(any(Employee.class))).thenReturn(employee);
        EmployeeEagerDTO updatedEmplyoee = employeeService.updateEmployee(employee);
        verify(employeeDAO).save(any(Employee.class));
        assertEquals(updatedEmplyoee.getId(),1);
    }

    @Test
    void updateEmployee_employeeNotFoundExceptionTest(){
        assertThrows(EmployeeNotFoundException.class,()->{
            employee.setId(2);
            when(employeeDAO.findById(2)).thenReturn(Optional.empty());
            EmployeeEagerDTO updateEmployee = employeeService.updateEmployee(employee);
            verify(employeeDAO).save(any(Employee.class));
            verify(employeeDAO).findById(anyInt());
        });
    }

    @Test
    void assignTicket() {
    }

    @Test
    void deleteEmployee() {
        employee.setFirstName("test name");
        when(employeeDAO.findById(1)).thenReturn(Optional.ofNullable(employee));
        employeeService.deleteEmployee(1);
        verify(employeeDAO).findById(anyInt());
    }

    @Test
    void deleteEmployee_employeeNotFoundTest(){
        assertThrows(EmployeeNotFoundException.class,()->{
            when(employeeDAO.findById(1)).thenReturn(Optional.empty());
            employeeService.deleteEmployee(1);
            verify(employeeDAO).findById(anyInt());
        });
    }

    @Test
    void deleteEmployee_deleteWithTicketTest(){
        assertThrows(EmployeeDeleteException.class,()->{
            Ticket ticket = new Ticket();
            employee.setEmployeeNumber(1);
            when(employeeDAO.findById(anyInt())).thenReturn(Optional.ofNullable(employee));
           when(ticketDAO.findByAssignee(1)).thenReturn(Collections.singletonList(ticket));
           employeeService.deleteEmployee(1);
           verify(ticketDAO).findByAssignee(anyInt());
        });
    }
}