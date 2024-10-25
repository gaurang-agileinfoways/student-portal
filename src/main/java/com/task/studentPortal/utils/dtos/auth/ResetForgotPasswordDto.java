package com.task.studentPortal.utils.dtos.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetForgotPasswordDto {

    @NotNull(message = "New password required.")
    private String newPassword;
}
