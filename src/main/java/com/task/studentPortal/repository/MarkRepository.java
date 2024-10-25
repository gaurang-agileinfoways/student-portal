package com.task.studentPortal.repository;

import com.task.studentPortal.model.Mark;
import com.task.studentPortal.utils.dtos.marks.MyMarkResponseDto;
import com.task.studentPortal.utils.dtos.marks.RankDto;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    //    Optional<List<Mark>> findByUser_Id(Long id);
//    Optional<List<Mark>> findBySubjectIdAndStudentId()

    @Query("SELECT m FROM Mark m WHERE m.subject.id = :subjectId AND m.student.id = :studentId")
    Mark findBySubjectAndStudent(@Param("subjectId") Long subjectId, @Param("studentId") Long studentId);

//    @Query("SELECT new com.task.studentPortal.utils.dtos.marks.MyMarkResponseDto(m.id, m.mark, s.name, s.standard) " +
//            "FROM Mark m JOIN m.subject s WHERE m.student.id = :studentId")
//    List<MyMarkResponseDto> findMyResult(@Param("studentId") Long studentId);

    @Query("SELECT new com.task.studentPortal.utils.dtos.marks.MyMarkResponseDto(m.id, m.mark, s.name) " +
            "FROM Mark m JOIN m.subject s WHERE m.student.id = :studentId AND s.standard = :standard")
    List<MyMarkResponseDto> findMyResult(@Param("studentId") Long studentId, @Param("standard") Integer standard);

    @Query(value =
            "WITH RankedStudents AS ( " +
                    "    SELECT " +
                    "        m.student_id, " +
                    "        SUM(m.mark) AS total_marks, " +
                    "        DENSE_RANK() OVER (ORDER BY SUM(m.mark) DESC) AS rank " +
                    "    FROM " +
                    "        mark m " +
                    "    JOIN " +
                    "        subject s ON m.subject_id = s.id " +
                    "    WHERE " +
                    "        s.standard = :standard " +
                    "    GROUP BY " +
                    "        m.student_id " +
                    ") " +
                    "SELECT " +
                    "rs.student_id, rs.total_marks, rs.rank " +
                    "FROM RankedStudents rs " +
                    "WHERE " +
                    "    rs.student_id = :studentId",
            nativeQuery = true)
    List<Tuple> getStudentRank(@Param("studentId") Long studentId, @Param("standard") Integer standard);

//    @Query(value = "SELECT new com.task.studentPortal.utils.dtos.marks.RankDto(m.student.id, SUM(m.mark), " +
//            "DENSE_RANK() OVER (ORDER BY SUM(m.mark) DESC)) " +
//            "FROM Mark m JOIN m.subject s " +
//            "WHERE s.standard = :standardId " +
//            "GROUP BY m.student.id " +
//            "HAVING m.student.id = :studentId")
//    List<RankDto> getStudentRank(@Param("studentId") Long studentId, @Param("standardId") Integer standardId);

}
