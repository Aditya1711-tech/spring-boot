package com.ess.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_subtasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SubTask {

    public enum SubTaskStatus {
        TODO, IN_PROGRESS, IN_REVIEW, DONE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtask_id")
    private long id;

    @Column(name = "subtask_description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    @JsonBackReference
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(name = "subtask_status")
    private SubTaskStatus status;

    public SubTask(String description, Task task, SubTaskStatus status) {
        this.description = description;
        this.task = task;
        this.status = status;
    }
}
