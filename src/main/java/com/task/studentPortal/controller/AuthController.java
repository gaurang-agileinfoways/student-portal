package com.task.studentPortal.controller;

import com.task.studentPortal.service.StudentService;
import com.task.studentPortal.utils.dtos.auth.ForgotPasswordDto;
import com.task.studentPortal.utils.dtos.auth.LoginRequestDto;
import com.task.studentPortal.utils.dtos.auth.RegisterStudentRequestDto;
import com.task.studentPortal.utils.dtos.auth.ResetForgotPasswordDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private StudentService studentService;

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid() @RequestBody LoginRequestDto body) {
        return this.studentService.loginUser(body);
    }

    @PostMapping(value = "register", consumes = "multipart/form-data")
    public ResponseEntity<?> register(@Valid() @RequestPart("data") RegisterStudentRequestDto body,
                                      @RequestPart("file") MultipartFile file) {
        return this.studentService.addStudent(body, file);
    }

    @PostMapping("forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid() @RequestBody ForgotPasswordDto body) {
        return this.studentService.forgotPassword(body);
    }

    @PostMapping("forgotPassword/{token}")
    public ResponseEntity<?> changeForgotPassword(@PathVariable("token") String token, @Valid() @RequestBody ResetForgotPasswordDto body) {
        return this.studentService.changeForgotPassword(token, body);
    }
}
