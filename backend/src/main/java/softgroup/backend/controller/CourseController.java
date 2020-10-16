package softgroup.backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softgroup.backend.entity.*;
import softgroup.backend.repository.Course_CommunityRepository;
import softgroup.backend.service.*;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class CourseController {
    @Resource
    CourseService courseService;

    @Resource
    UserService userService;

    @Resource
    VideoService videoService;

    @Resource
    Course_studentService course_studentService;

    @Resource
    CommunityService communityService;

    @Resource
    Course_CommunityService course_communityService;

    //创建课程
    @CrossOrigin
    @PostMapping(value = "/api/create")
    @ResponseBody
    public Result createcourse(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params){

        if(courseService.getByName(params.get("className")) != null){
            return new Result(400,"课程名已被占用");
        }

        Integer courseid =  courseService.addCourse(file,params);
        return new Result(200, "课程创建成功！", courseid);
    }

    //根据不同方式查找课程
    @CrossOrigin
    @PostMapping(value = "/api/searchcourse")
    @ResponseBody
    public Result searchcourse(@RequestBody Map<String, String> params){
        String  key = params.get("key");
        if (key.equals("1")){
             Course course = courseService.getByName(params.get("name"));
             return new Result(200, "查找成功", course);
        }
        else if(key.equals("2")){
            List<Course> l = courseService.getBySchool(params.get("school"));
            return new Result(200, "查找成功", l);
        }
        else if(key.equals("3")){
            User teacher = userService.getById(params.get("id"));
            List<Course> l = courseService.getByTeacher(teacher);
            return new Result(200, "查找成功", l);
        }
        return new Result(400, "查找失败");
    }

    //根据不同方式查找课程
    @CrossOrigin
    @PostMapping(value = "/api/searchcourse2")
    @ResponseBody
    public Result searchcourse2(@RequestBody Map<String, String> params){
        String  key = params.get("key");
        List<Course> course;
        List<String> teachername = new ArrayList<>();
        if(key.equals("0"))
        {
            course = courseService.getAll();
        }
        else if (key.equals("1")){//通过课程名查找
            course = courseService.SearchByName(params.get("keyword"));
        }
        else if(key.equals("2")){//通过老师名字查找
            course = courseService.getByTeachername(params.get("keyword"));
        }
        else if(key.equals("3")){//通过学校名称查找
             course = courseService.SearchBySchool(params.get("keyword"));
        }
        else if(key.equals("4")){//按学科查找
            course = courseService.getByType(params.get("keyword"));
        }
        else if(key.equals("5")){//查找该用户加入的所有课程
            List<Course_student> course_students=  course_studentService.getByUserid(params.get("keyword"));
            List<Course> courses = new ArrayList<Course>();
            for(int i = 0; i<course_students.size(); i++){
                courses.add(courseService.getById(course_students.get(i).getCourseId()));
            }
            return new Result(200, "课程列表查看成功", courses);
        }
        else if(key.equals("6")){// 根据教师id查找
            course = courseService.getByTeacherid(params.get("keyword"));
        }
        else if(key.equals("7")){
            List<Course_student> course_students =  course_studentService.getByUserid(params.get("keyword"));
            List<Course> courses = new ArrayList<>();
            for(int i = 0; i<course_students.size(); i++){
                courses.add(courseService.getById(course_students.get(i).getCourseId()));
            }
            return new Result(200, "参与列表查看成功", courses);
        }
        else if(key.equals("8")){
            Course course1 =  courseService.getById(Integer.valueOf(params.get("keyword")));
            Course_Community course_community =  course_communityService.findByCourseid(course1.getCourseid());
            Community community = communityService.getById(course_community.getCommunityid());
            return new Result(200, "课程及圈子查找成功", course1, community);
        }
        else{
            return new Result(400, "查找失败！");
        }
        for(int i = 0; i<course.size(); i++)
        {
            teachername.add(course.get(i).getTeachername());
        }
        return new Result(200, "查找成功", course, teachername);
    }

    @CrossOrigin
    @PostMapping(value = "/api/deletecourse")
    @ResponseBody
    public Result deletecourse(@RequestBody Map<String, String> params) {
        try{
            courseService.deleteCoursebyId(Integer.valueOf(params.get("courseid")));
            return new Result(200, "课程删除成功");
        }
        catch (Exception e){
            return new Result(400, e.toString());
        }
    }

    //主页推荐视频
    @CrossOrigin
    @PostMapping(value = "/api/recommend")
    @ResponseBody
    public Result recommendcourse(){
        Result res = courseService.recommendVideo();
        return res;
    }

    //根据课程id返回课程信息和视频
    @CrossOrigin
    @PostMapping(value = "/api/getvideo")
    @ResponseBody
    public Result getvideo(@RequestBody Map<String, String> params){
        Course course = new Course();
        course = courseService.getById(Integer.valueOf(params.get("classid")));
        List<Video> videos = videoService.getByCourse(course);
        Map<String, String> info = new HashMap<>();
        User teacher = course.getTeacher();
        info.put("teacher", teacher.getUsername());
        info.put("avatar", teacher.getAvatar());
        return new Result(200,"课程查看成功",videos, info);
    }

    //判断学生是否在课程里
    @CrossOrigin
    @PostMapping(value = "/api/judgestudentcourse")
    @ResponseBody
    public Result judgeStudent(@RequestBody Map<String, String> params){
        return new Result(400,"",courseService.judgebystudentid(params.get("studentid"),Integer.valueOf(params.get("courseid").toString())));
    }

    //学生加入课程
    @CrossOrigin
    @PostMapping(value = "/api/joinstudentcourse")
    @ResponseBody
    public Result joinCourse(@RequestBody Map<String, String> params){
        String studentid = params.get("studentid");
        User user = userService.getById(studentid);
        if(user == null){
            return new Result(250,"学号不存在");
        }
        return courseService.inviteOneToCourse(params);
    }

    //教师拉学生进入课程
    @CrossOrigin
    @PostMapping(value = "/api/studentintocourse")
    @ResponseBody
    public Result intocourse(@RequestBody Map<String, String> params){
        return courseService.intocourse(params.get("studentid"),Integer.valueOf(params.get("courseid")));
    }


    //学生退出课程
    @CrossOrigin
    @PostMapping(value = "/api/exitcourse")
    @ResponseBody
    public Result exitCourse(@RequestBody Map<String, String> params){
        try {
            courseService.exitCourse(params.get("studentid"),Integer.valueOf(params.get("courseid").toString()));
        }
        catch (Exception e){
            return new Result(400,"退出失败");
        }
        return new Result(200,"退出成功");
    }

    //显示该课程下的所有学生
    @CrossOrigin
    @PostMapping(value = "/api/getstudentlist")
    @ResponseBody
    public Result getStudentList(@RequestBody Map<String, String> params){
        return courseService.getStudentbyCourse(params);
    }

    //申请加入课程
//    @CrossOrigin
//    @PostMapping(value = "/api/apply_course")
//    @ResponseBody
//    public Result apply_course(@RequestBody Map<String, String> params){
//        try{
//            Course course = courseService.getById(Integer.valueOf(params.get("courseid")));
//            Applycourse applycourse = new Applycourse(params. get("userid"), Integer.valueOf(params.get("courseid")), course.getTeacher().getUserid());
//            applycourseService.submit(applycourse);
//        }
//        catch (Exception e)
//        {
//            return new Result(400, "申请失败");
//        }
//        return new Result(200, "申请成功");
//    }
}
