package com.task.studentPortal.utils.dtos.auth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterStudentRequestDto {

    @NotNull(message = "Enrollment number is required.")
    @Length(min = 10, max = 12, message = "Enrollment number must have 10 to 12 digits.")
    @Pattern(regexp = "^[0-9]+$", message = "Only digits are allowed.")
    private String enrollmentNumber;

    @NotNull(message = "Name is required.")
    private String name;

    @NotNull(message = "Email is required.")
    private String email;

    @NotNull(message = "Password is required.")
    private String password;

    @NotNull(message = "Standard is required.")
    @Min(value = 1, message = "Standard must be at least 1.")
    @Max(value = 12, message = "Standard must be at most 12.")
    private Integer standard;
}
