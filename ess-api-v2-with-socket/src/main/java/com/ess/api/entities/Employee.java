package com.ess.api.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
public class Employee{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private long id;
    
    @Column(name = "employee_firstname")
    private String firstName;

    @Column(name = "employee_lastname")
    private String lastName;
    
    @Column(name = "employee_email" ,unique = true)
    private String  email;

    @Column(name = "employee_password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Leave> leaves;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AttendanceCorrection> corrections;


    public Employee(String firstName, String lastName, String email, String password, Role role, Team team) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.team = team;
    }

    public Employee(long id, String firstName, String lastName, String email, String password, Role role, Team team, List<Leave> leaves) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.team = team;
        this.leaves = leaves;
    }

    public Employee() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Leave> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<Leave> leaves) {
        this.leaves = leaves;
    }

    public int getLeavesAllowed() {
        return leavesAllowed;
    }

    public List<AttendanceCorrection> getCorrections() {
        return corrections;
    }

    public void setCorrections(List<AttendanceCorrection> corrections) {
        this.corrections = corrections;
    }

    //leaves allowed throughout the year
    @Transient
    private final int leavesAllowed = 18;

    //extra methods for leaves
    public int getTotalLeavesTaken() {
        if (leaves == null) {
            return 0;
        }
        int approvedCnt = 0;
        for (Leave leaf : leaves) {
            if (leaf.getStatus().toString().equalsIgnoreCase("approved"))
                approvedCnt++;
        }
        return approvedCnt;
    }

    public int getTotalLeavesLeft(){
        if(leaves == null){
            return leavesAllowed;
        }

        return (leavesAllowed-this.getTotalLeavesTaken());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", team=" + team +
                ", leaves=" + leaves +
                ", corrections=" + corrections +
                ", leavesAllowed=" + leavesAllowed +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Employee other = (Employee) obj;
        if (id != other.id)
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        if (team == null) {
            if (other.team != null)
                return false;
        } else if (!team.equals(other.team))
            return false;
        if (leaves == null) {
            if (other.leaves != null)
                return false;
        } else if (!leaves.equals(other.leaves))
            return false;
        return true;
    }  
    
}
