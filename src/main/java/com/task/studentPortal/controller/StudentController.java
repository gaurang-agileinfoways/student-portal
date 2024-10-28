package com.task.studentPortal.controller;

import com.task.studentPortal.config.StudentUserDetails;
import com.task.studentPortal.service.StudentService;
import com.task.studentPortal.utils.dtos.auth.ChangePasswordDto;
import com.task.studentPortal.utils.dtos.students.UpdateProfileDto;
import com.task.studentPortal.utils.responseHandler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("student")
public class StudentController {

    private StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal StudentUserDetails userDetails) {

        userDetails.setPassword(null);
        return ResponseHandler.generateResponse(userDetails, "Profile fetched success");
    }

    @PutMapping("/")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDto body, @AuthenticationPrincipal StudentUserDetails userDetails) {
        return studentService.updateProfile(body, userDetails.getEmail());
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid() @RequestBody ChangePasswordDto body, @AuthenticationPrincipal StudentUserDetails userDetails) {
        return studentService.changePassword(body, userDetails.getEmail());
    }
}
