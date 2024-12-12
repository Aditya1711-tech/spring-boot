package com.ess.api.security.services;

import com.ess.api.entities.Employee;
import com.ess.api.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository
                            .findByEmail(email)
                            .orElseThrow(
                                    () -> new UsernameNotFoundException("Employee"));

        return UserDetailsImpl.build(employee);
    }
}
