package com.task.studentPortal.utils.dtos.students;

import lombok.Data;

@Data
public class UpdateProfileDto {
    private String name;
    private String email;
    private String standard;
}
