package softgroup.backend.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.*;
import softgroup.backend.service.*;
import softgroup.backend.repository.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class CommunityController {
    @Resource
    UserRepository userRepository;
    @Resource
    Community_studentRepository community_studentRepository;
    @Resource
    Course_studentRepository course_studentRepository;
    @Resource
    Course_CommunityRepository course_communityRepository;
    @Resource
    PostService postService;
    @Resource
    UserService userService;
    @Resource
    CommunityService communityService;
    @Resource
    CourseService courseService;
    @Resource
    Course_CommunityService course_communityService;

    /**
     * 创建社区
     * @author Ocean Sound
     * @param community 要添加的社区
     * @date 2020/9/9
     * @return Community
     */
    @GetMapping(value = "/api/createCommunity")
    JSONObject createCommunity(Community community){
        return JSONObject.parseObject(JSONObject.toJSONString(communityService.createCommunity(community)));
    }

    /**
     * 单独邀请成员
     * @author Ocean Sound
     * @param
     * @date 2020/9/9
     * @return
     */
    @GetMapping(value = "/api/inviteOne")
    JSONObject inviteOneToCommunity(Community community, User user){
        return JSONObject.parseObject(JSONObject.toJSONString(communityService.inviteOneToCommunity(community,user)));
    }

    //获取用户列表
    @CrossOrigin
    @PostMapping(value = "/api/getUserList")
    @ResponseBody
    public Result getUserList(@RequestBody Map<String, String> params){
        Integer  com_id = Integer.valueOf(params.get("com_id".toString()));
        Course_Community course_community = course_communityService.findByCommunityid(com_id);
        Integer course_id = course_community.getCourseid();
        Community community = communityService.getById(com_id);
        Course course = courseService.getById(course_id);
       // Integer course_id = course.getCourseid();
        List<Course_student> course_student = course_studentRepository.findByCourseId(course_id);
        List<User> students = new LinkedList<>();
        for(int i = 0;i < course_student.size();i++){
            String studentid = course_student.get(i).getStudentId();
            students.add(userRepository.findByUserid(studentid));
        }
        List<Community_student> community_student =community_studentRepository.findByCommunityId(com_id);
        for(int i = 0;i < community_student.size();i++){
            String com_studentid =community_student.get(i).getStudentId();
            students.add(userRepository.findByUserid(com_studentid));
        }
        return new Result(200, "成功",students);
    }

    //创建新社区
    @CrossOrigin
    @PostMapping(value = "/api/createNewCommunity")
    @ResponseBody
    public Result community(@RequestBody Map<String, String> params) throws ParseException {
        Community new_community = new Community();
        new_community.setName(params.get("name").toString());
        new_community.setRules("暂时没有规则");

        String userid = params.get("creator").toString();
        Integer i = Integer.valueOf(userid);
        User user = userService.getById(userid);
        new_community.setCreator(user);

        String courseid = params.get("courseid").toString();
        Integer ii = Integer.valueOf(courseid);
        Course_Community course_community = new Course_Community();
        course_community.setCourseid(ii);

        String name = params.get("name").toString();

        try{
            communityService.Community(new_community);
            Integer communityid = communityService.getByName(name).getId();
            course_community.setCommunityid(communityid);
            course_communityService.Course_Community(course_community);

            //老师自动加入社区
            Community_student community_student = new Community_student();
            community_student.setCommunityId(communityid);
            community_student.setStudentId(userid);
            community_studentRepository.save(community_student);

            return new Result(200, "发送成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }

    //修改社区信息
    @CrossOrigin
    @PostMapping(value = "/api/modifyCommunity")
    @ResponseBody
    public Result modifycommunity(@RequestBody Map<String, String> params) throws ParseException {
        //Community new_community = new Community();
        Community new_community = communityService.getById(Integer.valueOf(params.get("id").toString()));
        new_community.setName(params.get("name").toString());
        new_community.setRules(params.get("rules").toString());

        try{
            communityService.Community(new_community);
            return new Result(200, "修改成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }
}
