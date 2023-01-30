package com.exist.webhelpdesksystem.controller;

import com.exist.webhelpdesksystem.config.HelpdeskSystemAuthConfig;
import com.exist.webhelpdesksystem.entity.Role;
import com.exist.webhelpdesksystem.service.RoleService;
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

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.core.Is.is;
@SpringBootTest
@AutoConfigureMockMvc
@Tag("controller")
@Tag("rolecontroller")
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private RoleService roleService;

    @Test
    @WithMockUser(username = "testUser")
    void getAllRolesControllerTest() throws Exception {
        Role role = new Role();
        role.setName("CEO");
        when(roleService.getAllRoles()).thenReturn(Collections.singletonList(role));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/role"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data[0].name",is("CEO")));
    }

    @Test
    void getAllRolesControllerUnauthenticatedTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/role"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @WithMockUser(password="testPassword", roles={"ADMIN"}, username="testUser")
    void createRoleControllerTest() throws Exception {
        Role newRole = new Role();
        ObjectMapper objectMapper = new ObjectMapper();
        newRole.setName("CEO");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role")
                        .content(objectMapper.writeValueAsString(newRole))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createRoleControllerUnauthenticatedTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role").with(csrf())).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testUser",roles = "USER")
    void createRoleControllerUnauthorizedTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/role")).andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}