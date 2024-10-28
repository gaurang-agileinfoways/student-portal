package com.task.studentPortal.controller;

import com.task.studentPortal.config.StudentUserDetails;
import com.task.studentPortal.service.AttendanceService;
import com.task.studentPortal.utils.dtos.attendance.DateAttendanceDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("attendance")
@AllArgsConstructor
public class AttendanceController {

    private AttendanceService attendanceService;

    @GetMapping("")
    public ResponseEntity<?> getMyAttendance(@RequestBody DateAttendanceDto body,
                                             @AuthenticationPrincipal StudentUserDetails userDetails) {
        return attendanceService.getMyAttendance(body, userDetails.getId());
    }

    @PostMapping("attend")
    public ResponseEntity<?> addMyAttendance(@AuthenticationPrincipal StudentUserDetails userDetails) {
        return attendanceService.addMyAttendance(userDetails.getId());
    }

    @GetMapping("monthly")
    public ResponseEntity<?> getMonthlyAttendance(@AuthenticationPrincipal StudentUserDetails userDetails) {
        return attendanceService.getGetAttendance(userDetails.getId());
    }

}
