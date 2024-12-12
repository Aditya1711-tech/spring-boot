package com.ess.api.controllers;

import com.ess.api.entities.Employee;
import com.ess.api.request.LoginRequest;
import com.ess.api.repositories.EmployeeRepository;
import com.ess.api.response.JwtResponse;
import com.ess.api.security.jwt.JwtUtils;
import com.ess.api.security.services.UserDetailsImpl;
import com.ess.api.utils.GetCurrentEmployee;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginRequest loginRequest){
        /*Employee employee = employeeRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        System.out.println(encoder.matches(loginRequest.getPassword(), employee.getPassword()));
        System.out.println(encoder.encode(loginRequest.getPassword()));*/
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), currentEmployee.getRole().getName(), currentEmployee.getTeam().getName()));
    }
}
