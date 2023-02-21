package com.exist.webhelpdesksystem.controller;


import com.exist.webhelpdesksystem.dto.EmployeeEagerDTO;
import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.request.AssignTicketRequest;
import com.exist.webhelpdesksystem.service.EmployeeService;
import com.exist.webhelpdesksystem.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee")
    public ResponseEntity<Object> getEmployees(){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",employeeService.findAllEmployee());
    }


    @GetMapping("/employee/{id}")
    public ResponseEntity<Object> getEmployee(@PathVariable int id){
        EmployeeEagerDTO employeeEagerDTO = employeeService.findEmployee(id);
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",employeeEagerDTO);
    }

    @PostMapping("/employee")
    public ResponseEntity<Object> createEmployee(@RequestBody Employee employee){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"success",employeeService.createEmployee(employee));
    }

    @PutMapping("/employee")
    public ResponseEntity<Object> updateEmployee(@RequestBody Employee employee){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Employee updated successfully",employeeService.updateEmployee(employee));
    }

    @PutMapping("/employee/assign-ticket")
    public ResponseEntity<Object> assignTicket(@RequestBody AssignTicketRequest request){
        return ResponseUtil.generateGenericResponse(HttpStatus.OK,"Ticket assigned successfully", employeeService.assignTicket(request));
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable int id){
        employeeService.deleteEmployee(id);
        return ResponseUtil.generateGenericDeleteResponse(HttpStatus.OK,"Employee deleted successfully");
    }
}
