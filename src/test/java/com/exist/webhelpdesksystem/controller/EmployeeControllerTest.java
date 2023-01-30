package com.exist.webhelpdesksystem.controller;

import com.exist.webhelpdesksystem.entity.Employee;
import com.exist.webhelpdesksystem.request.AssignTicketRequest;
import com.exist.webhelpdesksystem.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("controller")
@Tag("employeecontroller")
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "USER")
    void getEmployees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/employee/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee() throws Exception{
        Employee employee = new Employee();
        ObjectMapper objectMapper = new ObjectMapper();
        employee.setFirstName("Test Name");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void createEmployeeUnauthenticatedTest() throws Exception{
        Employee employee = new Employee();
        ObjectMapper objectMapper = new ObjectMapper();
        employee.setFirstName("Test Name");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "USER")
    void createEmployeeUnauthorizedTest() throws Exception{
        Employee employee = new Employee();
        ObjectMapper objectMapper = new ObjectMapper();
        employee.setFirstName("Test Name");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEmployee() throws Exception {
        Employee employee = new Employee();
        ObjectMapper objectMapper = new ObjectMapper();
        employee.setFirstName("Test Name");
        employee.setId(1);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/employee")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateEmployeeUnauthenticatedTest() throws Exception {
        Employee employee = new Employee();
        ObjectMapper objectMapper = new ObjectMapper();
        employee.setFirstName("Test Name");
        employee.setId(1);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/employee")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "USER")
    void updateEmployeeUnauthorizedTest() throws Exception {
        Employee employee = new Employee();
        ObjectMapper objectMapper = new ObjectMapper();
        employee.setFirstName("Test Name");
        employee.setId(1);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/employee")
                        .content(objectMapper.writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignTicket() throws Exception {
        AssignTicketRequest requestBody = new AssignTicketRequest();
        requestBody.setEmployeeId(1);
        requestBody.setTicketId(1);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void assignTicketUnauthenticatedTest() throws Exception {
        AssignTicketRequest requestBody = new AssignTicketRequest();
        requestBody.setEmployeeId(1);
        requestBody.setTicketId(1);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "USER")
    void assignTicketUnauthorizedTest() throws Exception {
        AssignTicketRequest requestBody = new AssignTicketRequest();
        requestBody.setEmployeeId(1);
        requestBody.setTicketId(1);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/employee")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void deleteEmployeeUnauthenticatedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "USER")
    void deleteEmployeeUnauthorizedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employee/1")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}