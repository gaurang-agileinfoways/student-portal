package com.task.studentPortal.utils.dtos.students;

import lombok.Data;

@Data
public class StudentResponseDto {
    private Long id;
    private String enrollmentNumber;
    private String name;
    private String email;
    private String profilePicUrl;
    private Integer standard;
}
