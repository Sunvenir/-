package softgroup.backend.service;

import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softgroup.backend.entity.*;
import softgroup.backend.repository.*;
import softgroup.backend.util.MinioUtil;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CourseService {
    @Resource
    CourseRepository courseRepository;

    @Resource
    private MinioUtil minioUtil;

    @Resource
    UserRepository userRepository;

    @Resource
    VideoRepository videoRepository;

    @Resource
    Course_studentRepository course_studentRepository;

    @Resource
    Video_studentRepository video_studentRepository;

    private final String bucketName = "videos";

    public Course getByName(String name){return courseRepository.findByCoursename(name);}

    public List<Course> SearchByName(String name){return courseRepository.findByCoursenameLike("%"+name+"%");}

    public List<Course> getBySchool(String school){return courseRepository.findByCourseschool(school);}

    public List<Course> SearchBySchool(String school){return courseRepository.findByCourseschoolLike("%"+school+"%");}

    public List<Course> getByTeacher(User teacher){return courseRepository.findByTeacher(teacher);}

    public List<Course> getByTeachername(String name){return courseRepository.findByTeachernameLike("%"+name+"%");}

    public List<Course> getByTeacherid(String teacherid){
        User teacher = userRepository.findByUserid(teacherid);
        return courseRepository.findByTeacher(teacher);
    }

    public List<Course> getByType(String type){return courseRepository.findByCoursetype(type);}

    public List<Course> getAll(){return courseRepository.findByCourseidNotNull();}

    public Course getById(Integer id){return courseRepository.findById(id).get();}

    public Integer addCourse(MultipartFile file, Map<String, String> params){
        User teacher = userRepository.findByUserid(params.get("teacher".toString()));

        Course course = new Course();
        course.setTeacher(teacher);
        course.setTeachername(teacher.getUsername());
        course.setCoursename(params.get("className".toString()));
        course.setCourseschool(params.get("schoolName").toString());
        course.setCoursetype(params.get("type").toString());
        course.setCourseintro(params.get("brief"));
        course.setCoursecover(params.get("cover").toString());
        course.setParticipant(0);

        String filename = file.getOriginalFilename();
        String url = minioUtil.upload(bucketName, file, filename);

        Video video = new Video();
        video.setVideoName(params.get("videoName"));
        video.setVideoUptime(new Date());
        video.setVideoType(params.get("type"));
        video.setStorePath(url);
        video.setPlaySum("0");
        video.setCommentSum("0");
        video.setSupportSum("0");
        video.setUploader(userRepository.findByUserid(params.get("teacher")));
        video.setVideotime(Integer.valueOf(params.get("time")));
        //course.addtime(Integer.valueOf(params.get("time"))); //将视频时长添加到课程时长

        course.setTeacher(userRepository.findByUserid(params.get("teacher")));
        course.setCoursetime(Integer.valueOf(params.get("time")));

        courseRepository.save(course);
        video.setCourse(course);
        videoRepository.save(video);

        return courseRepository.findByCoursename(params.get("className")).getCourseid();
    }

    //返回四个推荐视频
    public Result recommendVideo(){
        Map<String, Course>teacher_course = new HashMap<>();
        List<Course> tempcourses = new LinkedList<>();
        List<Course> courses = new LinkedList<>();
        List<User> teachers = new LinkedList<>();
        tempcourses = courseRepository.findAll();
        Collections.shuffle(tempcourses);
        for(int i = 0;i < Math.min(tempcourses.size(), 4);i++){
            Course course =  tempcourses.get(i);
            courses.add(course);
            teachers.add(course.getTeacher());
            String teacher = course.getTeacher().getUsername();
            //teacher_course.put(teacher, course);
        }
        Result result = new Result();
        result.setData(courses);
        result.setDetail(teachers);
        result.setCode(200);
        result.setMessage("tuijianchenggong");
        return result;
    }

    //添加一人进入课程
    public Result inviteOneToCourse(Map<String, String> params){
        Course_student course_student = new Course_student();
        course_student.setStudentId(params.get("studentid".toString()));
        course_student.setCourseId(Integer.valueOf(params.get("courseid")));
        course_studentRepository.save(course_student);
        //课程参与人数+1s
        Course course = courseRepository.findByCourseid(Integer.valueOf(params.get("courseid")));
        int sum = course.getParticipant();
        sum++;
        course.setParticipant(sum);
        courseRepository.save(course);
        return new Result(200,"成功");
    }

    //显示该课程下的所有学生
    public Result getStudentbyCourse(Map<String, String> params){
        Integer courseid = Integer.valueOf(params.get("courseid".toString()));
        List<Course_student> course_student = course_studentRepository.findByCourseId(courseid);
        List<User> students = new LinkedList<>();
        for(int i = 0;i < course_student.size();i++){
            String studentid = course_student.get(i).getStudentId();
            students.add(userRepository.findByUserid(studentid));
        }
        return new Result(200, "成功",students);
    }

    //删除课程
    public Result deleteCoursebyId(Integer id){
        Course course = courseRepository.findByCourseid(id);
        List<Video> videos = videoRepository.findAllByCourse(course);
        System.out.println(videos.size());
        for(int i = 0;i < videos.size();i++){
            System.out.println(videos.get(i).getVideoId());
            videoRepository.deleteByVideoId(videos.get(i).getVideoId());
            video_studentRepository.deleteByVideoid(videos.get(i).getVideoId());
        }
        courseRepository.deleteByCourseid(id);
        course_studentRepository.deleteByCourseId(id);
        return new Result(200, "成功");
    }

    //判断学生是否在课程里面
    public boolean judgebystudentid(String studentid,Integer courseid){
        boolean judge = false;
        List<Course_student> temp = course_studentRepository.findByCourseId(courseid);
        for(int i = 0;i < temp.size();i++){
            if(studentid.equals(temp.get(i).getStudentId())){
                judge = true;
            }
        }
        return judge;
    }

    //学生退出课程
    public void exitCourse(String studentid,Integer courseid){
        course_studentRepository.deleteByStudentIdAndCourseId(studentid,courseid);
        Course course = courseRepository.findByCourseid(courseid);
        int sum = course.getParticipant();
        sum--;
        course.setParticipant(sum);
        courseRepository.save(course);
    }

    //教师拉学生进入课程
    public Result intocourse(String students,Integer courseid){
        int flag1 = 0, flag2 = 0;
        String[] temp = students.split("@");
        for(int i = 0;i < temp.length;i++){
            User user = userRepository.findByUserid(temp[i]);
            if(user.getUsertype() != null && user.getUsertype() == 1){
                flag1++;
                continue;
            }
            if(course_studentRepository.findByStudentIdAndAndCourseId(temp[i],courseid) != null){
                flag2++;
                continue;
            }
            Course_student course_student = new Course_student();
            course_student.setStudentId(temp[i]);
            course_student.setCourseId(courseid);
            course_studentRepository.save(course_student);
        }
        //更新课程参与人数
        Course course = courseRepository.findByCourseid(courseid);
        int sum =  course.getParticipant();
        sum += temp.length - flag1 - flag2;
        course.setParticipant(sum);
        courseRepository.save(course);

        if(flag1 > 0 && flag2 > 0)
            return new Result(400,"用户中存在老师，且有人已经在课程中，其余用户已添加");
        else if(flag1 > 0)
            return new Result(400,"用户中存在老师，其余用户已添加");
        else if(flag2 > 0)
            return new Result(400, "用户中有的已经在课程中，其余用户已添加");
        else
            return new Result(200, "添加成功");
    }
}
