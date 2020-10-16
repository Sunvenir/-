package softgroup.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import softgroup.backend.entity.Community;
import softgroup.backend.entity.Course_student;
import softgroup.backend.entity.Result;
import softgroup.backend.entity.User;
import softgroup.backend.repository.CommunityRepository;
import softgroup.backend.entity.*;
import softgroup.backend.repository.*;

import javax.annotation.Resource;
import java.net.HttpRetryException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map;

@Service
public class CommunityService {
    @Autowired
    private CommunityRepository communityRepository;

    @Resource
    private Community_studentRepository community_studentRepository;

    @Resource
    Course_CommunityService course_communityService;

    /**
     * 创建社区
     * @author Ocean Sound
     * @param community 要创建的社区
     * @date 2020/9/9
     * @return Result
     */
    public Result<HttpStatus> createCommunity(Community community) {

        Result<HttpStatus> result = new Result<>();
        try {
            communityRepository.save(community);
            result.setCode(200);
            result.setMessage("Add Community Successfully!");
            result.setDetail(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            result.setCode(401);
            result.setMessage("Fail to add community!");
            result.setDetail(HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    /**
     * 邀请单个人
     * @author Ocean Sound
     * @param community 将要添加到
     * @param user 被邀请者
     * @date 2020/9/9
     * @return Result
     */
    public Result<HttpStatus> inviteOneToCommunity(Community community, User user){
        Result<HttpStatus> result = new Result<>(406, "");
        try {
            //community.getUsers().add(user);
            communityRepository.save(community);
            result.setMessage("邀请成功");
            result.setCode(200);
            result.setDetail(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            e.printStackTrace();
            result.setCode(406);
            result.setMessage("邀请失败");
            result.setDetail(HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    //根据ID查找社区
    public Community getById(Integer id) {
        return communityRepository.findById(id);
    }

    //添加一人进入课程
    public Result inviteOneToCommunity(Map<String, String> params){
        Community_student community_student = new Community_student();
        community_student.setStudentId(params.get("studentid".toString()));

        Integer courseid = Integer.valueOf(params.get("courseid").toString());
        Course_Community course_community = course_communityService.findByCourseid(courseid);
        Integer communityid= course_community.getCommunityid();

        community_student.setCommunityId(communityid);
        community_studentRepository.save(community_student);
        return new Result(200,"成功");
    }

    public void Community(Community community){
        communityRepository.save(community);
    }

    public Community getByName(String name) {
        return communityRepository.findByName(name);
    }
}
