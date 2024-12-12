package com.ess.api.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_logs")
public class ProjectLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "log_message")
    private String message;

    @Column(name = "log_timestamp")
    private LocalDateTime timestamp;

    public ProjectLog() {
        this.timestamp = LocalDateTime.now();
    }

    public ProjectLog(Project project, String message) {
        this.project = project;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ProjectLog{" +
                "id=" + id +
                ", project=" + project +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
