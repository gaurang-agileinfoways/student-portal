package com.task.studentPortal.utils.dtos.students;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
