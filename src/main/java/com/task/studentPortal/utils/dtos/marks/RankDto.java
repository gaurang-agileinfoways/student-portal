package com.task.studentPortal.utils.dtos.marks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankDto {
    private Long studentId;
    private Long totalMark;
    private Long rank;
}
