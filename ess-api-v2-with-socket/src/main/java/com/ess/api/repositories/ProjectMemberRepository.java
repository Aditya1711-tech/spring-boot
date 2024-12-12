package com.ess.api.repositories;

import com.ess.api.entities.Employee;
import com.ess.api.entities.Project;
import com.ess.api.entities.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    public ProjectMember findByEmployeeAndProject(Employee employee, Project project);
//    public List<Employee> findListOfMembersFromProjectByRole(Project project, Project.RoleInProject role);
}
