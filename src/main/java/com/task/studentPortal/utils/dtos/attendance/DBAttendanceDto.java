package com.task.studentPortal.utils.dtos.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DBAttendanceDto {
    private Long id;

    private Long studentId;

    private Date date;

    private String status;
}
