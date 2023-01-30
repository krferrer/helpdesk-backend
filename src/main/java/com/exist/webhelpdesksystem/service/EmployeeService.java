package com.exist.webhelpdesksystem.service;

import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.dao.TicketDAO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Ticket;
import com.exist.webhelpdesksystem.exception.EmployeeDeleteException;
import com.exist.webhelpdesksystem.exception.EmployeeNotFoundException;
import com.exist.webhelpdesksystem.exception.TicketNotFoundException;
import com.exist.webhelpdesksystem.request.AssignTicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Employee createEmployee(Employee employee){
        String requestPassword = employee.getPassword();
        String hashedPassword = passwordEncoder.encode(requestPassword);
        employee.setPassword(hashedPassword);
        return employeeDAO.save(employee);
    }
    public Iterable<Employee> findAllEmployee(){
        return employeeDAO.findAll();
    }

    public Employee findEmployee(int id){
        Optional<Employee> employee = employeeDAO.findById(id);
        if(!employee.isPresent()){
            throw new EmployeeNotFoundException("No employee with the id of: " + id);
        }
        return employee.get();
    }

    public Employee updateEmployee(Employee reqEmployee){
        Optional<Employee> optionalEmployee = employeeDAO.findById(reqEmployee.getId());
        if(!optionalEmployee.isPresent()){
            throw new EmployeeNotFoundException("Can't update employee, invalid id: " + reqEmployee.getId());
        }
        Employee employee = optionalEmployee.get();
        Employee newEmployee = new Employee();
        newEmployee.setId(reqEmployee.getId());
        newEmployee.setUsername(reqEmployee.getUsername());
        newEmployee.setRole(reqEmployee.getRole());
        newEmployee.setEmployeeNumber(reqEmployee.getEmployeeNumber());
        newEmployee.setFirstName(reqEmployee.getFirstName());
        newEmployee.setLastName(reqEmployee.getLastName());
        newEmployee.setMiddleName(reqEmployee.getMiddleName());
        newEmployee.setDepartment(reqEmployee.getDepartment());
        newEmployee.setWatchId(employee.getWatchId());
        newEmployee.setPassword(employee.getPassword());
        return employeeDAO.save(newEmployee);
    }

    public Ticket assignTicket(AssignTicketRequest request){
        Integer employeeId = request.getEmployeeId();
        Integer ticketId = request.getTicketId();
        Optional<Employee> optionalEmployee=employeeDAO.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new EmployeeNotFoundException("Employee not found");
        }
        Employee employee= optionalEmployee.get();
        if(ticketId==null){
            List<Ticket> tickets = ticketDAO.findByAssignee(employee.getEmployeeNumber());
            if(tickets.size() != 0){
                Ticket foundTicket = tickets.get(0);
                foundTicket.setAssignee(null);
                return ticketDAO.save(foundTicket);
            }else{
                return null;
            }
        }
        Optional<Ticket> optionalTicket = ticketDAO.findById(ticketId);
        if(!optionalTicket.isPresent()){
            throw new TicketNotFoundException("Ticket not found");
        }
        Ticket foundTicket = optionalTicket.get();
        foundTicket.setAssignee(employee);
        return ticketDAO.save(foundTicket);
    }

    public void deleteEmployee(int employeeId){
        Optional<Employee> optionalEmployee = employeeDAO.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new EmployeeNotFoundException("No employee with the id of: " + employeeId);
        }
        int employeeNumber = optionalEmployee.get().getEmployeeNumber();
        List<Ticket> tickets = ticketDAO.findByAssignee(employeeNumber);
        Integer watchId = optionalEmployee.get().getWatchId();
        if(!tickets.isEmpty() || watchId !=null) {
            throw new EmployeeDeleteException("Can not delete employees with assigned tickets");
        }
        employeeDAO.deleteById(employeeId);
    }
}
