package com.task.studentPortal.utils.dtos.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DBGetAttendanceCountDto {

    private Long studentId;
    private Long attendedDays;
}
