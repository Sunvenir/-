package softgroup.backend.service;

import org.springframework.stereotype.Service;
import softgroup.backend.entity.Course_student;
import softgroup.backend.repository.Course_studentRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
public class Course_studentService {
    @Resource
    Course_studentRepository course_studentRepository;

    public Course_student getByCourseidAndUserid(String userid, Integer courseid){
        return course_studentRepository.findByStudentIdAndAndCourseId(userid, courseid);
    }

    public List<Course_student> getByUserid(String userid){return course_studentRepository.findByStudentId(userid);}

    public void save(Course_student course_student){
        course_studentRepository.save(course_student);
    }

}
