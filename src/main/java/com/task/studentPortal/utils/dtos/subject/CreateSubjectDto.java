package com.task.studentPortal.utils.dtos.subject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSubjectDto {

    @NotNull(message = "Email is required.")
    private String name;

    @NotNull(message = "Standard is required.")
    @Min(value = 1, message = "Standard must be at least 1.")
    @Max(value = 12, message = "Standard must be at most 12.")
    private Integer standard;
}
