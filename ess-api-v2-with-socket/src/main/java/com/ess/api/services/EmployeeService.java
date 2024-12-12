package com.ess.api.services;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Role;
import com.ess.api.entities.Team;
import com.ess.api.exceptions.ResourceAlreadyExistsException;
import com.ess.api.exceptions.ResourceNotFoundException;
import com.ess.api.repositories.EmployeeRepository;
import com.ess.api.repositories.RoleRepository;
import com.ess.api.request.UpdatePasswordRequest;
import com.ess.api.response.ApiResponse;
import com.ess.api.utils.SendMail;
import jakarta.mail.MessagingException;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SendMail sendMail;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Employee getEmployeeByEmailVulnerable(String email) {
        // Unsafe query using string concatenation (SQL injection-prone)
        String sql = "SELECT * FROM employees WHERE employee_email = '" + email + "'";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Employee.class));
    }

    // Add Employee
    public Employee addEmployee(Employee employee){
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(existingEmployee.isPresent()){
            throw new ResourceAlreadyExistsException("Employee", " Employee email ", employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    // Update by id
    public Employee updateEmployee(Long empId, Employee employee){
        Employee employeeWithId = this.getEmployee(empId);
        if(employee.getFirstName() != null) employeeWithId.setFirstName(employee.getFirstName());
        if(employee.getLastName() != null) employeeWithId.setLastName(employee.getLastName());
        if(employee.getEmail() != null) employeeWithId.setEmail(employee.getEmail());
        if(employee.getPassword() != null) employeeWithId.setPassword(employee.getPassword());
        if(employee.getRole() != null) employeeWithId.setRole(employee.getRole());
        if(employee.getTeam() != null) employeeWithId.setTeam(employee.getTeam());
        if(employee.getLeaves() != null) employeeWithId.setLeaves(employee.getLeaves());

        return employeeRepository.save(employeeWithId);
    }


    // Get all
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    // Get by id
    public Employee getEmployee(long empId){
        return employeeRepository.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Employee", "EmpId", "" + empId));
    }

    // Get by email
    public Employee getEmployeeByEmail(String empEmail){
        try{
            return employeeRepository.findByEmail(empEmail).orElseThrow(() -> new ResourceNotFoundException("Employee", "Email", empEmail));
        }catch (Exception e){
            throw new ResourceNotFoundException("Employee", "Email", empEmail);
        }
    }

    // Get employees from given team
    public List<Employee> getEmployeesFromGivenTeam(Long teamId){
        Team teamOfGivenId = teamService.GetTeamById(teamId);
        return employeeRepository.findAllEmployeesByTeam(teamOfGivenId);
    }

    // Get employees with given role
    public List<Employee> getEmployeesWithGivenRole(Long roleId){
        Role roleWithGivenId = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "RoleId", roleId.toString()));
        return employeeRepository.findAllEmployeesByRole(roleWithGivenId);
    }

    // Get UpdatePassword Email
    public ApiResponse getUpdatePasswordEmail(UpdatePasswordRequest updatePasswordRequest) throws MessagingException, IOException {
        if(updatePasswordRequest.getEmail() == null || updatePasswordRequest.getEmail().trim().isEmpty()){
            return new ApiResponse("Must provide valid email.", false);
        }
        Employee employee = this.getEmployeeByEmail(updatePasswordRequest.getEmail());
        sendMail.sendResetPasswordMail(employee.getEmail(), employee.getFirstName() + " " + employee.getLastName());
        return new ApiResponse("Link for update password sent to: " + employee.getEmail(), true);
    }

    // Update password
    public ApiResponse updatePassword(UpdatePasswordRequest updatePasswordRequest){
        Employee employee = this.getEmployeeByEmail(updatePasswordRequest.getEmail());
        if(updatePasswordRequest.getOldPassword() == null || updatePasswordRequest.getNewPassword() == null ||updatePasswordRequest.getOldPassword().trim().isEmpty() || updatePasswordRequest.getNewPassword().isEmpty()){
            return new ApiResponse("You must provide old and new password both.", false);
        }
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), employee.getPassword())){
            return new ApiResponse("Wrong old password!!", false);
        }
        if(updatePasswordRequest.getOldPassword().trim().equals(updatePasswordRequest.getNewPassword().trim())){
            return new ApiResponse("Choose different password than previous password.", false);
        }

        employee.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        employeeRepository.save(employee);
        return new ApiResponse("Your password is updated successfully.", true);
    }
}
