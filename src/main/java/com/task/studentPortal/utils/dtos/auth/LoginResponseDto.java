package com.task.studentPortal.utils.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String enrollmentNumber;
    private String name;
    private String email;
    private String profilePicUrl;
    private Integer standard;
    private String accessToken;
}
