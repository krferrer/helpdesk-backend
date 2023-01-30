package com.exist.webhelpdesksystem.service;

import com.exist.webhelpdesksystem.dao.RoleDAO;
import com.exist.webhelpdesksystem.entity.Role;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.Assert.*;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Tag("service")
@Tag("roleservice")
class RoleServiceTest {
    @Mock
    RoleDAO roleDAO;

    @InjectMocks
    RoleService roleService;

    @Test
    void getAllRolesTest() {
        Role role = new Role();
        role.setName("CEO");
        when(roleDAO.findAll()).thenReturn(Collections.singletonList(role));
        Iterable<Role> roles = roleService.getAllRoles();
        assertEquals(roles.iterator().next().getName(),"CEO");
        verify(roleDAO).findAll();
    }

    @Test
    void createRoleTest() {
        Role role = new Role();
        role.setName("CEO");
        when(roleDAO.save(any(Role.class))).thenReturn(role);
        Role foundRole  = roleService.createRole(role);
        assertEquals(foundRole.getName(),"CEO");
        verify(roleDAO).save(any(Role.class));
    }
}