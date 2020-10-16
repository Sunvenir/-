package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.Community;
import softgroup.backend.entity.Course_student;

import java.util.List;

public interface Course_studentRepository extends JpaRepository<Course_student, Integer> {
    List<Course_student> findByCourseId(Integer courseid);

    List<Course_student> findByStudentId(String studentid);

    Course_student findByStudentIdAndAndCourseId(String studentid,Integer courseid);

    @Transactional
    void deleteByCourseId(Integer courseid);

    @Transactional
    void deleteByStudentIdAndCourseId(String studentid,Integer courseid);
}
