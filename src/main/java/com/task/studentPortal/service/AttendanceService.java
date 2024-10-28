package com.task.studentPortal.service;

import com.task.studentPortal.model.Attendance;
import com.task.studentPortal.model.MonthlyAttendance;
import com.task.studentPortal.model.Student;
import com.task.studentPortal.repository.AttendanceRepository;
import com.task.studentPortal.repository.MonthlyAttendanceRepository;
import com.task.studentPortal.repository.StudentRepository;
import com.task.studentPortal.utils.constants.AttendanceConstant;
import com.task.studentPortal.utils.dtos.attendance.DBAttendanceDto;
import com.task.studentPortal.utils.dtos.attendance.DBGetAttendanceCountDto;
import com.task.studentPortal.utils.dtos.attendance.DateAttendanceDto;
import com.task.studentPortal.utils.exceptions.customExceptions.AlreadyExistException;
import com.task.studentPortal.utils.exceptions.customExceptions.AuthException;
import com.task.studentPortal.utils.exceptions.customExceptions.NotFoundException;
import com.task.studentPortal.utils.responseHandler.ResponseHandler;
import com.task.studentPortal.utils.services.CommonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class AttendanceService {

    private AttendanceRepository attendanceRepository;
    private StudentRepository studentRepository;
    private MonthlyAttendanceRepository monthlyAttendanceRepository;

    public ResponseEntity<?> getMyAttendance(DateAttendanceDto body, Long id) {

        System.out.println(body);
        if (body.getStartDate() == null && body.getEndDate() == null) {
            LocalDateTime startDateTime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0);
            body.setStartDate(Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant()));

            LocalDateTime endDateTime = startDateTime.plusHours(24);
            body.setEndDate(Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }

        List<DBAttendanceDto> attendance = attendanceRepository.findByStartDateAndEndDate(body.getStartDate(), body.getEndDate(), id);
        return ResponseHandler.generateResponse(attendance, "Attendance fetched success");
    }

    public ResponseEntity<?> addMyAttendance(Long id) {
        LocalDateTime startDateTime = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0);
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime endDateTime = startDateTime.plusHours(24);
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        List<DBAttendanceDto> attendance = attendanceRepository.findByStartDateAndEndDate(startDate, endDate, id);

        if (!attendance.isEmpty()) {
            throw new AlreadyExistException("Attendance already added");
        }

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AuthException("Student not found."));

        Attendance attend = new Attendance();
        attend.setDate(new Date());
        attend.setStatus(AttendanceConstant.PRESENT);
        attend.setStudent(student);
        return ResponseHandler.generateResponse(attendanceRepository.save(attend), "Attendance added success");
    }

    public ResponseEntity<?> getGetAttendance(Long studentId) {
        return ResponseHandler.generateResponse(
                monthlyAttendanceRepository.findByStudentId(studentId), "Student attendance fetched.");
    }

    // execute every month last date
    @Scheduled(cron = "0 59 23 L * ?")
//    @Scheduled(cron = "0 * * * * *")
    private void attendanceMonthlySchedule() {
        Calendar calendar = Calendar.getInstance();
        System.out.println("Calendar: " + calendar);

        // Set to the first day of this month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1); // Move to last month
        Date startDate = calendar.getTime(); // Start of last month
        
        // Set to the last day of last month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime(); // End of last month

        Integer workingDays = CommonService.countWorkingDays(calendar);
        List<DBGetAttendanceCountDto> attendance = attendanceRepository.getAttendance(startDate, endDate);

        Set<MonthlyAttendance> monthlyAttendances = new HashSet<>();
        for (DBGetAttendanceCountDto attendances : attendance) {
            long avg = (attendances.getAttendedDays() * 100) / workingDays;

            System.out.println("student" + attendances);

            String attendanceResult = avg > 90 && avg <= 100 ? "5*"
                    : avg > 75 && avg <= 90 ? "4*"
                    : avg > 29 && avg <= 75 ? "PASS" : "FAIL";

            Student s = studentRepository.findById(attendances.getStudentId()).orElseThrow(() -> new NotFoundException("Student not found"));
            MonthlyAttendance ma = new MonthlyAttendance(attendanceResult, workingDays, attendances.getAttendedDays(), startDate, endDate, s);
            monthlyAttendances.add(ma);
        }

        monthlyAttendanceRepository.saveAll(monthlyAttendances);
        Logger.getLogger("Attendance Cron successfully.");
    }


}
