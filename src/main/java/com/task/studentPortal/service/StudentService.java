package com.task.studentPortal.service;

import com.task.studentPortal.model.Student;
import com.task.studentPortal.repository.StudentRepository;
import com.task.studentPortal.utils.dtos.auth.*;
import com.task.studentPortal.utils.dtos.students.UpdateProfileDto;
import com.task.studentPortal.utils.exceptions.customExceptions.*;
import com.task.studentPortal.utils.responseHandler.ResponseHandler;
import com.task.studentPortal.utils.services.CommonService;
import com.task.studentPortal.utils.services.EmailServices;
import com.task.studentPortal.utils.tokenGeneration.JwtTokenGenerator;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    private ModelMapper modelMapper;
    private StudentRepository studentRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenGenerator jwtTokenGenerator;
    private EmailServices emailServices;
    private CommonService commonService;

    public ResponseEntity<?> addStudent(@RequestBody RegisterStudentRequestDto body, @NotNull MultipartFile file) {

        if (file.isEmpty()) {
            throw new NotFoundException("Image not found.");
        }
        Optional<Student> isExist = studentRepository.findByEnrollmentNumberOrEmail(body.getEnrollmentNumber(), body.getEmail());
        if (isExist.isPresent()) {
            throw new AlreadyExistException("Student already exist, Sign in now!!");
        }
        Student student = modelMapper.map(body, Student.class);

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        // Create the file path
        String fileName = body.getEnrollmentNumber().concat(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")));
        File fileToSave = new File(uploadDir + fileName);
        try {
            // Save the file
            file.transferTo(fileToSave);
        } catch (IOException e) {
            // Handle the error appropriately
            throw new InternalServerException(e.getMessage());
        }
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setProfilePicUrl(fileToSave.getAbsolutePath());
        return ResponseHandler.generateResponse(studentRepository.save(student), "Student registration success.");
    }

    public ResponseEntity<?> loginUser(LoginRequestDto body) {

        Student student = studentRepository.findByEnrollmentNumberOrEmail(body.getEnrollmentOrEmail(), body.getEnrollmentOrEmail())
                .orElseThrow(() -> new AuthException("Invalid email or enrollment number."));

        System.out.println(student);
        LoginResponseDto resp = modelMapper.map(student, LoginResponseDto.class);
        resp.setAccessToken(jwtTokenGenerator.generateToken(body, student));
        return ResponseHandler.generateResponse(resp, "Student login success.");
    }

    public ResponseEntity<?> forgotPassword(ForgotPasswordDto body) {
        Student student = studentRepository.findByEnrollmentNumberOrEmail(body.getEnrollmentOrEmail(), body.getEnrollmentOrEmail())
                .orElseThrow(() -> new AuthException("Invalid email or enrollment number."));

        String randomString = commonService.generateRandomString();

        student.setForgotPasswordToken(randomString);
        studentRepository.save(student);
        try {
            emailServices.sendEmail(student.getEmail(), "Forgot password", "your forgot password token is: " + randomString);
        } catch (MessagingException e) {
            throw new InternalServerException("Error in email sending, please try again!!");
        }
        return ResponseHandler.generateResponse(null, "Email send successfully.");
    }

    public ResponseEntity<?> changeForgotPassword(String token, ResetForgotPasswordDto body) {
        Student student = studentRepository.findByForgotPasswordToken(token)
                .orElseThrow(() -> new AuthException("Invalid forgot password token."));

        student.setPassword(passwordEncoder.encode(body.getNewPassword()));
        student.setForgotPasswordToken(null);
        studentRepository.save(student);
        return ResponseHandler.generateResponse(null, "New password set successfully.");
    }

    public ResponseEntity<?> getProfile(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found."));

        return ResponseHandler.generateResponse(student, "User profile fetch successfully.");
    }

    public ResponseEntity<?> changePassword(ChangePasswordDto body, String email) {
        if (body.getOldPassword().equals(body.getNewPassword())) {
            throw new BadRequestException("New password same as old password.");
        }

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Student not found."));

        if (!passwordEncoder.matches(body.getOldPassword(), student.getPassword())) {
            throw new AuthException("Invalid old password!!");
        }

        student.setPassword(passwordEncoder.encode(body.getNewPassword()));
        studentRepository.save(student);
        return ResponseHandler.generateResponse(null, "Password update successfully.");
    }

    public ResponseEntity<?> updateProfile(UpdateProfileDto body, String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found."));
        modelMapper.map(body, student);
        return ResponseHandler.generateResponse(null, "Student update successfully.");
    }
}
