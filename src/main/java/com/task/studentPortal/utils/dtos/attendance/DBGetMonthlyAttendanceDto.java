package com.task.studentPortal.utils.dtos.attendance;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DBGetMonthlyAttendanceDto {
    private Long id;
    private String status;
    private Long totalDay;
    private Long attendedDay;
    private Date startDate;
    private Date endDate;
    private Long student;
}
