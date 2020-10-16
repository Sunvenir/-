package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.Course;
import softgroup.backend.entity.User;

import java.beans.Transient;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course> {

    Course findByCoursename(String coursename);

    List<Course> findByCoursenameLike(String coursename);

    List<Course> findByCourseschool(String schoolname);

    List<Course> findByCourseschoolLike(String schoolname);

    List<Course> findByTeacher(User teacher);

    List<Course> findByTeachernameLike(String teachername);


    List<Course> findByCoursetype(String type);

    List<Course> findByCourseidNotNull();

    Course findByCourseid(Integer course_id);

    @Transactional
    void deleteByCourseid(Integer id);


}
