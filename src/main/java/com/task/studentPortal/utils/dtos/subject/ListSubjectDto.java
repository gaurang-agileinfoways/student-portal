package com.task.studentPortal.utils.dtos.subject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class ListSubjectDto {
    private String name;

    @Min(value = 1, message = "Standard must be at least 1.")
    @Max(value = 12, message = "Standard must be at most 12.")
    private Integer standard;

}
