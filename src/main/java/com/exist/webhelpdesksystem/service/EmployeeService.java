package com.exist.webhelpdesksystem.service;

import com.exist.webhelpdesksystem.dao.EmployeeDAO;
import com.exist.webhelpdesksystem.dao.TicketDAO;
import com.exist.webhelpdesksystem.dto.EmployeeEagerDTO;
import com.exist.webhelpdesksystem.dto.EmployeeLazyDTO;
import com.exist.webhelpdesksystem.dto.TicketLazyDTO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.entity.Ticket;
import com.exist.webhelpdesksystem.exception.EmployeeDeleteException;
import com.exist.webhelpdesksystem.exception.EmployeeNotFoundException;
import com.exist.webhelpdesksystem.exception.TicketNotFoundException;
import com.exist.webhelpdesksystem.request.AssignTicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private TicketService ticketService;


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
    public List<EmployeeLazyDTO> findAllEmployee(){
        List<EmployeeLazyDTO> employeeDTOs = employeeDAO.findAll()
                .stream().map((employee)->{
                    EmployeeLazyDTO employeeLazyDTO = new EmployeeLazyDTO();
                    return employeeLazyDTO.employeeToLazyEmployee(employee);
                }).collect(Collectors.toList());
        return employeeDTOs;
    }


    @Transactional
    public EmployeeEagerDTO findEmployee(int id){
        Optional<Employee> optionalEmployee = employeeDAO.findById(id);
        if(!optionalEmployee.isPresent()){
            throw new EmployeeNotFoundException("No employee with the id of: " + id);
        }
        Employee employee = optionalEmployee.get();
        EmployeeEagerDTO employeeDTO = new EmployeeEagerDTO();
        List<Ticket> tickets = ticketDAO.findByAssignee(employee.getEmployeeNumber());
        TicketLazyDTO ticketLazyDTO = new TicketLazyDTO();
        if(!tickets.isEmpty()){
            ticketLazyDTO.ticketToLazyDTO(tickets.get(0));
            employeeDTO.setAssignedTicket(ticketLazyDTO);
        }
        return employeeDTO.employeeToEagerEmployee(employee);
    }

    @Transactional
    public EmployeeEagerDTO updateEmployee(Employee reqEmployee){
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
        newEmployee.setPassword(employee.getPassword());
        newEmployee.setWatchTickets(employee.getWatchTickets());
        EmployeeEagerDTO employeeEagerDTO = new EmployeeEagerDTO();
        employeeEagerDTO.employeeToEagerEmployee(employeeDAO.save(newEmployee));
        return employeeEagerDTO;
    }

    public TicketLazyDTO assignTicket(AssignTicketRequest request){
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
                TicketLazyDTO ticketLazyDTO = new TicketLazyDTO();
                ticketLazyDTO.ticketToLazyDTO(ticketDAO.save(foundTicket));
                return ticketLazyDTO;
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
        TicketLazyDTO ticketLazyDTO = new TicketLazyDTO();
        ticketLazyDTO.ticketToLazyDTO(ticketDAO.save(foundTicket));
        return ticketLazyDTO;
    }

    public void deleteEmployee(int employeeId){
        Optional<Employee> optionalEmployee = employeeDAO.findById(employeeId);
        if(!optionalEmployee.isPresent()){
            throw new EmployeeNotFoundException("No employee with the id of: " + employeeId);
        }
        int employeeNumber = optionalEmployee.get().getEmployeeNumber();
        List<Ticket> tickets = ticketDAO.findByAssignee(employeeNumber);
        if(!tickets.isEmpty()) {
            throw new EmployeeDeleteException("Can not delete employees with assigned tickets");
        }
        employeeDAO.deleteById(employeeId);
    }


}
