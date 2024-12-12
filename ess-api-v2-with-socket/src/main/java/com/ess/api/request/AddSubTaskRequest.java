package com.ess.api.request;

import com.ess.api.entities.SubTask;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AddSubTaskRequest {
    private String description;
    private SubTask.SubTaskStatus status;
}
