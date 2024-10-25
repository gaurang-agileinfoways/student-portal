package com.task.studentPortal.utils.dtos.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordDto {
    @NotNull(message = "Enter Enrollment number or Email.")
    private String enrollmentOrEmail;
}
