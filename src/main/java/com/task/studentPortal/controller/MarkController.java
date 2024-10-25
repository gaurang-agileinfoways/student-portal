package com.task.studentPortal.controller;

import com.task.studentPortal.config.StudentUserDetails;
import com.task.studentPortal.service.MarkService;
import com.task.studentPortal.utils.dtos.marks.CreateSingleMarksDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marks")
@AllArgsConstructor
public class MarkController {

    private MarkService markService;

    @PostMapping("add-single-marks")
    public ResponseEntity<?> addSingleMarks(@Valid @RequestBody CreateSingleMarksDto body) {
        return markService.addSingleMarks(body);
    }

    @GetMapping("my")
    public ResponseEntity<?> getMyMarks(@RequestParam("standard") Integer standard,
                                        @AuthenticationPrincipal StudentUserDetails userDetails) {
        return markService.getMyMarks(standard, userDetails.getId());
    }
}
