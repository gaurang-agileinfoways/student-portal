package com.task.studentPortal.utils.dtos.attendance;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class DateAttendanceDto {

    private Timestamp startDate;
    private Timestamp endDate;
}
