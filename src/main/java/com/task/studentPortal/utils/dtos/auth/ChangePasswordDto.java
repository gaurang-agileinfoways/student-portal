package com.task.studentPortal.utils.dtos.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @NotNull(message = "Old password must be string.")
    private String oldPassword;

    @NotNull(message = "New password must be string.")
    private String newPassword;
}
