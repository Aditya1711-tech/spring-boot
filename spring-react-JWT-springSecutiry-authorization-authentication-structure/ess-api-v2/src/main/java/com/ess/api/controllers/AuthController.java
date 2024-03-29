package com.ess.api.controllers;

import com.ess.api.request.LoginRequest;
import com.ess.api.repositories.EmployeeRepository;
import com.ess.api.response.JwtResponse;
import com.ess.api.security.jwt.JwtUtils;
import com.ess.api.security.services.UserDetailsImpl;
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

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginRequest loginRequest){
        System.out.println(encoder.encode(loginRequest.getPassword()));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.println("Converting.......");
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println("Converting2.......");
        System.out.println("Details: " + userDetails);
        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
    }
}
