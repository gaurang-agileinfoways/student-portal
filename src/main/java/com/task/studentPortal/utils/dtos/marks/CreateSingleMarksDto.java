package com.task.studentPortal.utils.dtos.marks;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSingleMarksDto {
    @NotNull(message = "student id required")
    private Long studentId;

    @NotNull(message = "subject id required")
    private Long subjectId;

    @NotNull(message = "marks required")
    @Max(value = 100, message = "maximum 100 marks allowed")
    @Min(value = 0, message = "minimum 0 marks allowed")
    private Integer marks;
}
