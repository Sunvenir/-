package softgroup.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import softgroup.backend.entity.*;
import softgroup.backend.repository.Community_studentRepository;
import softgroup.backend.service.*;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class ApplyCommunityController {
    @Resource
    ApplycommunityService applycommunityService;

    @Resource
    UserService userService;

    @Resource
    Course_CommunityService course_communityServiceService;

    @Resource
    CommunityService communityService;

    @Resource
    Community_studentRepository community_studentRepository;

    //用户提交申请
    @CrossOrigin
    @PostMapping(value = "/api/communityapplysubmit")
    @ResponseBody
    public Result submit(@RequestBody Map<String, String> params) {
        Integer courseid = Integer.valueOf(params.get("courseid").toString());
        Course_Community course_community = course_communityServiceService.findByCourseid(courseid);
        Integer communityid= course_community.getCommunityid();
        if(applycommunityService.getbyUseridAndCommunityid(params.get("userid"), communityid) != null)
        {
            return new Result(400, "您已经提交过审核了！");
        }
        else if(community_studentRepository.findByStudentIdAndAndCommunityId(params.get("userid"),communityid) != null)
        {
            return new Result(250, "已经在社区中");
        }
        else
        {
            Community community = communityService.getById(communityid);
            User user = userService.getById(params.get("userid"));
            String username = user.getUsername();
            Applycommunity applycommunity = new Applycommunity(params.get("userid"),communityid ,username);
            applycommunityService.submit(applycommunity);
            return new Result(200, "申请成功");
        }

    }

    //获取申请列表
    @CrossOrigin
    @PostMapping(value = "/api/getcommunityapplylist")
    @ResponseBody
    public Result get_communityapplylist(@RequestBody Map<String, String> params) {
        Integer courseid = Integer.valueOf(params.get("courseid").toString());
        Course_Community course_community = course_communityServiceService.findByCourseid(courseid);
        Integer communityid= course_community.getCommunityid();

        return new Result(200, "列表返回成功！", applycommunityService.getListbycommunityid(communityid));
    }

    //管理员通过审核
    @CrossOrigin
    @PostMapping(value = "/api/communityapplyaccept")
    @ResponseBody
    public Result success(@RequestBody Map<String, String> params) {
        Integer courseid = Integer.valueOf(params.get("courseid").toString());
        Course_Community course_community = course_communityServiceService.findByCourseid(courseid);
        Integer communityid= course_community.getCommunityid();

        applycommunityService.deletebyUseridAndCommunityid(params.get("studentid"),communityid);
        return communityService.inviteOneToCommunity(params);
    }

    //管理员拒绝审核
    @CrossOrigin
    @PostMapping(value = "/api/communityapplyrefuse")
    @ResponseBody
    public Result refuse(@RequestBody Map<String, String> params) {
        //String userid = params.get("userid");
        Integer courseid = Integer.valueOf(params.get("courseid").toString());
        Course_Community course_community = course_communityServiceService.findByCourseid(courseid);
        Integer communityid= course_community.getCommunityid();

        try{
            applycommunityService.deletebyUseridAndCommunityid(params.get("studentid"),communityid);
        }
        catch (Exception e){
            return new Result(400,"审核失败");
        }
        return new Result(200, "拒绝申请成功");
    }


}
