package softgroup.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import softgroup.backend.entity.*;
import softgroup.backend.service.ApplycourseService;
import softgroup.backend.service.CourseService;
import softgroup.backend.service.UserService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class ApplyCourseController {
    @Resource
    ApplycourseService applycourseService;

    @Resource
    UserService userService;

    @Resource
    CourseService courseService;

    //用户提交申请
    @CrossOrigin
    @PostMapping(value = "/api/courseapplysubmit")
    @ResponseBody
    public Result submit(@RequestBody Map<String, String> params) {
        if(applycourseService.getbyUseridAndCourseid(params.get("userid"), Integer.valueOf(params.get("courseid"))) == null)
        {
            Course course = courseService.getById(Integer.valueOf(params.get("courseid").toString()));
            User user = userService.getById(params.get("userid"));
            String username = user.getUsername();
            String teacherid = course.getTeacher().getUserid();
            Applycourse applycourse = new Applycourse(params.get("userid"),Integer.valueOf(params.get("courseid").toString()) ,teacherid,username);
            applycourseService.submit(applycourse);
            return new Result(200, "申请成功");
        }
        else
        {
            return new Result(400, "您已经提交过审核了！");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/getcourseapplylist")
    @ResponseBody
    public Result get_courseapplylist(@RequestBody Map<String, String> params) {

        return new Result(200, "列表返回成功！", applycourseService.getListbycourseid(Integer.valueOf(params.get("courseid").toString())));
    }

    //管理员通过审核
    @CrossOrigin
    @PostMapping(value = "/api/courseapplyaccept")
    @ResponseBody
    public Result success(@RequestBody Map<String, String> params) {
//        User user = userService.getById(params.get("userid").toString());
//        if(applycourseService.getbyUserid(params.get("userid").toString()) == null)
//            return new Result(400, "错误，没有这个申请");
        applycourseService.deletebyUseridAndCourseid(params.get("studentid"),Integer.valueOf(params.get("courseid".toString())));
        return courseService.inviteOneToCourse(params);
    }

    //管理员拒绝审核
    @CrossOrigin
    @PostMapping(value = "/api/courseapplyrefuse")
    @ResponseBody
    public Result refuse(@RequestBody Map<String, String> params) {
        //String userid = params.get("userid");
        try{
            applycourseService.deletebyUseridAndCourseid(params.get("studentid"),Integer.valueOf(params.get("courseid".toString())));
        }
        catch (Exception e){
            return new Result(400,"审核失败");
        }
        return new Result(200, "拒绝申请成功");
    }


}
