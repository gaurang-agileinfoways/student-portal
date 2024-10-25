package com.task.studentPortal.utils.dtos.marks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyMarkResponseDto {
    private Long id;
    private Integer mark;
    private String subject;
}
