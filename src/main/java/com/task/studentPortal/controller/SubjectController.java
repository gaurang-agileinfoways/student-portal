package com.task.studentPortal.controller;

import com.task.studentPortal.service.SubjectService;
import com.task.studentPortal.utils.dtos.subject.CreateSubjectDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("subject")
@AllArgsConstructor
public class SubjectController {

    private SubjectService subjectService;

    @PostMapping("")
    public ResponseEntity<?> addSubject(@RequestBody CreateSubjectDto body) {
        return subjectService.addSubject(body);
    }

    @GetMapping("")
    public ResponseEntity<?> getSubject(@RequestParam(value = "standard", required = false) Integer standard) {
        return subjectService.getSubject(standard);
    }
}
