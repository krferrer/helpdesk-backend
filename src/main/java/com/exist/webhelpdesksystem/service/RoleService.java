package com.exist.webhelpdesksystem.service;

import com.exist.webhelpdesksystem.dao.RoleDAO;
import com.exist.webhelpdesksystem.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDAO roleDAO;

    public Iterable<Role> getAllRoles(){
        return roleDAO.findAll();
    }

    public Role createRole(Role role){
        return roleDAO.save(role);
    }
}
