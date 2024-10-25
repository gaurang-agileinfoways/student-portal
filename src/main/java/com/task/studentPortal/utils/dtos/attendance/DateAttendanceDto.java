package com.task.studentPortal.utils.dtos.attendance;

import lombok.Data;

import java.util.Date;

@Data
public class DateAttendanceDto {

    private Date startDate;
    private Date endDate;
}
