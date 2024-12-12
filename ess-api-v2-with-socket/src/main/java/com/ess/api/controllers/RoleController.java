package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.response.ApiResponse;
import com.ess.api.entities.Role;
import com.ess.api.services.RoleService;
import com.ess.api.utils.GetCurrentEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "http://localhost:5173")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    // Add
    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role){
        Role newRole = roleService.addRole(role);
        return ResponseEntity.ok(newRole);
    }

    // Update
    @PutMapping("/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestBody Role role){
        Role updatedRole = roleService.updateRoleById(roleId, role);
        return ResponseEntity.ok(updatedRole);
    }

    //Delete
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId){
        Role roleToDelete = roleService.getRoleById(roleId);
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(roleToDelete);
    }

    //Get all Roles
    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        if(!currentEmployee.getRole().getName().equalsIgnoreCase("admin")){
            ApiResponse response = new ApiResponse("You are not authorized", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        List<Role> allRoles = roleService.getAllRoles();
        return ResponseEntity.ok(allRoles);
    }

    // Get By id
    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long roleId){
        Role role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    // Delete all
    /*
    @DeleteMapping("/all")
    public String deleteAll(){
        roleService.deleteAll();
        return "Deleted";
    }
     */

}
