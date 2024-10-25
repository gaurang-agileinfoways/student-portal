package com.task.studentPortal.service;

import com.task.studentPortal.model.Mark;
import com.task.studentPortal.model.Student;
import com.task.studentPortal.model.Subject;
import com.task.studentPortal.repository.MarkRepository;
import com.task.studentPortal.repository.StudentRepository;
import com.task.studentPortal.repository.SubjectRepository;
import com.task.studentPortal.utils.dtos.marks.CreateSingleMarksDto;
import com.task.studentPortal.utils.dtos.marks.MyMarkResponseDto;
import com.task.studentPortal.utils.exceptions.customExceptions.AlreadyExistException;
import com.task.studentPortal.utils.exceptions.customExceptions.NotFoundException;
import com.task.studentPortal.utils.responseHandler.ResponseHandler;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MarkService {

    private MarkRepository markRepository;
    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;

    public ResponseEntity<?> addSingleMarks(CreateSingleMarksDto body) {

        Student student = studentRepository.findById(body.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        Subject subject = subjectRepository.findById(body.getSubjectId())
                .orElseThrow(() -> new NotFoundException("Subject not found"));
        Mark opMark = markRepository.findBySubjectAndStudent(body.getSubjectId(), body.getStudentId());
        if (opMark != null) {
            throw new AlreadyExistException("Marks already exist.");
        }

        Mark mark = new Mark();
        mark.setMark(body.getMarks());
        mark.setStudent(student);
        mark.setSubject(subject);
        return ResponseHandler.generateResponse(markRepository.save(mark), "Marks added successfully.");
    }

    public ResponseEntity<?> getMyMarks(Integer standard, Long studentId) {

        List<MyMarkResponseDto> opMarks = markRepository.findMyResult(studentId, standard);
        if (opMarks.isEmpty()) {
            return ResponseHandler.generateResponse(null, "data not found");
        }

        List<Tuple> rank = markRepository.getStudentRank(studentId, standard);
        Map<String, Object> resp = new HashMap<>();
        Long total = 0L;
        for (MyMarkResponseDto myMark : opMarks) {
            total += myMark.getMark();
        }
        total = (total * 100) / (opMarks.size() * 100L);
        String grade = total <= 100 && total >= 80 ? "A grade"
                : total < 80 && total >= 60 ? "B grade"
                : total < 60 && total >= 29 ? "C grade" : "fail";
        resp.put("percent", total);
        resp.put("grade", grade);
        resp.put("rank", rank.get(0).get(2));
        resp.put("standard", standard);
        resp.put("subjects", opMarks);

        return ResponseHandler.generateResponse(resp, "Marks retrieved success");
    }


}
