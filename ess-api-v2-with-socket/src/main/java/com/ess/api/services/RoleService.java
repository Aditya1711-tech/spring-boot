package com.ess.api.services;

import com.ess.api.entities.Role;
import com.ess.api.exceptions.ResourceNotFoundException;
import com.ess.api.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Add role
    public Role addRole(Role role){
        return roleRepository.save(role);
    }

    // Get role
    public Role getRoleById(long roleId){
        return roleRepository
                .findById(roleId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Role", "RoleId", ""+roleId));
    }

    // Update Role
    public Role updateRoleById(Long roleId, Role role){
        Role roleWithId = this.getRoleById(roleId);
        if(role.getName() != null) roleWithId.setName(role.getName());
        if(role.getClearance() != null) roleWithId.setClearance(role.getClearance());
        if(role.getDescription() != null) roleWithId.setDescription(role.getDescription());

        return roleRepository.save(roleWithId);
    }

    // Get all roles
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    // Delete with id
    public void deleteRole(Long roleId){
        Role roleWithId = this.getRoleById(roleId);
        roleRepository.delete(roleWithId);
    }

    // Delete all
    public void deleteAll(){
        roleRepository.deleteAll();
    }
}
