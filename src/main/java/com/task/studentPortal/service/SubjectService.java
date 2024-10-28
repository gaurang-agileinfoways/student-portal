package com.task.studentPortal.service;

import com.task.studentPortal.model.Subject;
import com.task.studentPortal.repository.SubjectRepository;
import com.task.studentPortal.utils.dtos.subject.CreateSubjectDto;
import com.task.studentPortal.utils.exceptions.customExceptions.AlreadyExistException;
import com.task.studentPortal.utils.responseHandler.ResponseHandler;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubjectService {

    private SubjectRepository subjectRepository;
    private ModelMapper modelMapper;

    public ResponseEntity<?> addSubject(CreateSubjectDto body) {

        Optional<Subject> opSubject = subjectRepository.findByNameAndStandard(body.getName().trim().toLowerCase(), body.getStandard());

        if (opSubject.isPresent()) {
            throw new AlreadyExistException("Subject already exist");
        }

        Subject sub = modelMapper.map(body, Subject.class);
        sub.setName(sub.getName().trim().toLowerCase());
        return ResponseHandler.generateResponse(subjectRepository.save(sub), "Subject added successfully.");
    }

    public ResponseEntity<?> getSubject(CreateSubjectDto body) {
        List<Subject> subjects = subjectRepository.findAll();
        return ResponseHandler.generateResponse(subjects, "Subject retrieved success.");
    }
}
