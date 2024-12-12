package com.ess.api.controllers;

import com.ess.api.entities.*;
import com.ess.api.request.AddEmployeeRequest;
import com.ess.api.request.UpdatePasswordRequest;
import com.ess.api.response.ApiResponse;
import com.ess.api.response.EmployeeAnalysisResponse;
import com.ess.api.security.services.UserDetailsImpl;
import com.ess.api.security.services.UserDetailsServiceImpl;
import com.ess.api.services.*;
import com.ess.api.utils.GetCurrentEmployee;
import com.ess.api.utils.SendMail;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private GetCurrentEmployee getCurrentEmployee;

    @Autowired
    private SendMail sendMail;

    @Autowired
    private EmployeePersonalDetailsService employeePersonalDetailsService;

    @Autowired
    private PunchInAndOutService punchInAndOutService;

    @GetMapping("/by-email")
    public ResponseEntity<Employee> getEmployeeByEmail(@RequestParam String email) {
        Employee employee = employeeService.getEmployeeByEmailVulnerable(email);
        return ResponseEntity.ok(employee);
    }

    // Add
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody AddEmployeeRequest newEmployee) throws MessagingException, IOException {
        String password = newEmployee.getPassword();
        newEmployee.setPassword(passwordEncoder.encode(newEmployee.getPassword()));
        Role role = roleService.getRoleById(newEmployee.getRoleId());
        Team team = teamService.GetTeamById(newEmployee.getTeamId());
        Employee employeeToAdd = new Employee(newEmployee.getFirstName(), newEmployee.getLastName(),
                newEmployee.getEmail(), newEmployee.getPassword(), role, team);

        Employee newAddedEmployee = employeeService.addEmployee(employeeToAdd);

        String fullName = newAddedEmployee.getFirstName() + " " + newAddedEmployee.getLastName();
        String email = newAddedEmployee.getEmail();

        sendMail.sendWelcomeMail(fullName, email, password);
        return ResponseEntity.ok(newAddedEmployee);
    }

    // Get all
    @GetMapping("/all")
    public ResponseEntity<?> getAllEmployees(HttpSession session, Authentication authentication) {
        /*
         * Authentication authentication =
         * SecurityContextHolder.getContext().getAuthentication();
         * if (authentication != null && authentication.getPrincipal() instanceof
         * UserDetails) {
         * UserDetailsImpl userDetails = (UserDetailsImpl)
         * authentication.getPrincipal();
         * String useremail = userDetails.getEmail();
         * } else {
         * System.out.println("No user logged in.");
         * }
         */
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        /*
         * 
         * if (!currentEmployee.getRole().getName().equalsIgnoreCase("admin")) {
         * ApiResponse response = new ApiResponse("You are not authorized", false);
         * return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
         * }
         */
        List<Employee> allEmployees = employeeService.getAllEmployees();
        return ResponseEntity.ok(allEmployees);
    }

    // Get by id
    @GetMapping("/{empId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long empId) {
        Employee employee = employeeService.getEmployee(empId);
        return ResponseEntity.ok(employee);
    }

    // Get currentEmployee Data
    @GetMapping("/getCurrent")
    public ResponseEntity<Employee> getCurrentEmployee(Authentication authentication) {
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        currentEmployee.setPassword("XXXXXXXX");
        return ResponseEntity.ok(currentEmployee);
    }

    // Update by id
    @PutMapping("/{empId}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable Long empId, @RequestBody AddEmployeeRequest employee) {
        Employee employeeWithId = employeeService.getEmployee(empId);
        Team team = teamService.GetTeamById(employee.getTeamId());
        Role role = roleService.getRoleById(employee.getRoleId());
        employeeWithId.setTeam(team);
        employeeWithId.setRole(role);
        Employee updatedEmployee = employeeService.updateEmployee(empId, employeeWithId);
        return ResponseEntity.ok(updatedEmployee);
    }

    // Get updatePassword email
    @PostMapping("/updatePassword/getEmail")
    public ResponseEntity<?> getUpdatePasswordEmail(@RequestBody UpdatePasswordRequest updatePasswordRequest) throws MessagingException, IOException {
        ApiResponse response = employeeService.getUpdatePasswordEmail(updatePasswordRequest);
        if(!response.isSuccess()){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Update password
    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest){
        ApiResponse response = employeeService.updatePassword(updatePasswordRequest);
        if(!response.isSuccess()){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Login
    @PostMapping("/login")

    public ResponseEntity<?> loginEmployee(@RequestBody Employee employee, HttpSession session) {
        Employee employeeByEmail = employeeService.getEmployeeByEmail(employee.getEmail());
        if (employeeByEmail == null || !Objects.equals(employeeByEmail.getPassword(), employee.getPassword())) {
            ApiResponse response = new ApiResponse("Wrong email or password", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        employeeByEmail.setPassword("XXXXXXX");
        session.setAttribute("LoggedInUser", employeeByEmail);
        return ResponseEntity.ok(employeeByEmail);
    }

    // get all employees from given team
    @GetMapping("/team/{teamId}/all")
    public ResponseEntity<?> getAllTheEmployeesInGivenTeam(@PathVariable long teamId, Authentication authentication){
        List<Employee> employeesWithGivenTeam = employeeService.getEmployeesFromGivenTeam(teamId);
        return ResponseEntity.ok(employeesWithGivenTeam);
    }

    // get all employees with given role
    @GetMapping("/role/{roleId}/all")
    public ResponseEntity<?> getALlEmployeesWithGivenRole(@PathVariable long roleId, Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        if(!currentEmployee.getRole().getName().equalsIgnoreCase("admin")){
            ApiResponse response = new ApiResponse("You are not authorized", false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        List<Employee> employeesWithGivenRole = employeeService.getEmployeesWithGivenRole(roleId);
        return ResponseEntity.ok(employeesWithGivenRole);
    }

    // Add personal details
    @PostMapping("/personalDetails")
    public ResponseEntity<?> addEmployeePersonalDetails(@RequestBody EmployeePersonalDetails employeePersonalDetails, Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        EmployeePersonalDetails addedPersonalDetails = employeePersonalDetailsService.addDetails(currentEmployee, employeePersonalDetails);
        return ResponseEntity.ok(addedPersonalDetails);
    }

    // Get personal details of current employee
    @GetMapping("/personalDetails/me")
    public ResponseEntity<?> geTPersonalDetailsOfCurrentEmployee(Authentication authentication){
        Employee currentEmployee = getCurrentEmployee.getCurrentEmployee(authentication);
        EmployeePersonalDetails existingEmployeePersonalDetails = employeePersonalDetailsService.getByEmployee(currentEmployee);
        return ResponseEntity.ok(existingEmployeePersonalDetails);
    }

    // Get personal details from employeeId
    @GetMapping("/personalDetails/givenId/{employeeId}")
    public ResponseEntity<?> geTPersonalDetailsFromEmployeeId(@PathVariable long employeeId){
        Employee employeeWithId = employeeService.getEmployee(employeeId);
        EmployeePersonalDetails existingEmployeePersonalDetails = employeePersonalDetailsService.getByEmployee(employeeWithId);
        return ResponseEntity.ok(existingEmployeePersonalDetails);
    }

    // Get analysis
    @GetMapping("/getAnalysis/{employeeId}/{year}/{month}")
    public ResponseEntity<?> getEmployeeAnalysis(@PathVariable long employeeId, @PathVariable int year, @PathVariable int month){
        Employee employee = employeeService.getEmployee(employeeId);
        EmployeeAnalysisResponse employeeAnalysisResponse = punchInAndOutService.getAnalysisOfEmployee(employee, year, month);
        return ResponseEntity.ok(employeeAnalysisResponse);
    }
}
