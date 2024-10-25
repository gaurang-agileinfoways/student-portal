package com.task.studentPortal.controller;

import com.task.studentPortal.service.StudentService;
import com.task.studentPortal.utils.dtos.auth.ChangePasswordDto;
import com.task.studentPortal.utils.dtos.students.UpdateProfileDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("student")
public class StudentController {

    private StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("USER: " + userDetails);
//        return studentService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok("done");
    }

    @PutMapping("/")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileDto body, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("USER: " + userDetails);
        return studentService.updateProfile(body, userDetails.getUsername());
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid() @RequestBody ChangePasswordDto body, @AuthenticationPrincipal UserDetails userDetails) {
        return studentService.changePassword(body, userDetails.getUsername());
    }
}
