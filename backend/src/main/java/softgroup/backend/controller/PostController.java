package softgroup.backend.controller;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.*;
import softgroup.backend.repository.CommentRepository;
import softgroup.backend.repository.Course_CommunityRepository;
import softgroup.backend.repository.PostRepository;
import softgroup.backend.service.Course_CommunityService;
import softgroup.backend.service.UserService;
import softgroup.backend.service.CommunityService;
import softgroup.backend.repository.CommunityRepository;
import softgroup.backend.service.PostService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {
    @Resource
    PostService postService;
    @Resource
    UserService userService;
    @Resource
    CommunityService communityService;
    @Resource
    CommentRepository commentRepository;
    @Resource
    Course_CommunityService course_communityService;
    @Resource
    PostRepository postRepository;
    //发送普通帖
    @CrossOrigin
    @PostMapping(value = "/api/post")
    @ResponseBody
    public Result post(@RequestBody Map<String, String> params) throws ParseException {
        Post new_post = new Post();
        new_post.setHead(params.get("head").toString());
        new_post.setText(params.get("text").toString());

        String strtime = params.get("time").toString();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = ft.parse(strtime);
        new_post.setTime(date);

        String userid = params.get("poster").toString();
        Integer i = Integer.valueOf(userid);
        User user = userService.getById(userid);
        new_post.setPoster(user);

        Integer courseid = Integer.valueOf(params.get("courseid").toString());
        Course_Community course_community = course_communityService.findByCourseid(courseid);
        Integer communityid= course_community.getCommunityid();
        Integer comid =communityid;
        new_post.setCommunity(communityService.getById(comid));

        try{
            postService.Post(new_post);
            return new Result(200, "发送成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }

    //点赞帖子
    @CrossOrigin
    @PostMapping(value = "/api/likepost")
    @ResponseBody
    public Result likepost(@RequestBody Map<String, String> params) {
        String postid = params.get("postid").toString();
        Integer i = Integer.valueOf(postid);
        Post post = postService.getByPostid(i);
        post.like();

        try{
            postService.Post(post);
            return new Result(200, "点赞成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }

    //置顶帖子
    @CrossOrigin
    @PostMapping(value = "/api/toppost")
    @ResponseBody
    public Result toppost(@RequestBody Map<String, String> params) {
        String postid = params.get("postid").toString();
        Integer i = Integer.valueOf(postid);
        Post post = postService.getByPostid(i);
        int j = 1;
        post.setIstop(j);

        try{
            postService.Post(post);
            return new Result(200, "置顶成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }

    //取消置顶帖子
    @CrossOrigin
    @PostMapping(value = "/api/canceltop")
    @ResponseBody
    public Result cancelpost(@RequestBody Map<String, String> params) {
        String postid = params.get("postid").toString();
        Integer i = Integer.valueOf(postid);
        Post post = postService.getByPostid(i);
        int j = 0;
        post.setIstop(j);
        try{
            postService.Post(post);
            return new Result(200, "取消置顶成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }
    @CrossOrigin
    @PostMapping(value = "/api/deletepost")
    @ResponseBody
    public Result deletePostbyId(@RequestBody Map<String, String> params){
        String postid = params.get("postid").toString();
        Integer id = Integer.valueOf(postid);
        Post post = postService.getByPostid(id);
//        List<Comment> commentList = commentRepository.findAllByCommonPost(id);
        List<Comment> commentList = post.showComment();
        for(int i = 0; i < commentList.size(); i ++) {
            commentRepository.deleteByComid(commentList.get(i).getComid());
        }
        postRepository.deleteById(id);
        return new Result(200, "删除成功！！");

//        Integer courseid = Integer.valueOf(params.get("courseid").toString());
//        Course_Community course_community = course_communityService.findByCourseid(courseid);
//        Integer communityid = course_community.getCommunityid();

//        Community community = communityService.getById(communityid);
//        List<Post> post_list = community.showList();
//        for(int i = 0;i < post_list.size();i++){
//            if(post_list.get(i).getPostid()==id) {
//                post_list.remove(i);
//                break;
//            }
//            return new Result(400, "查询不到帖子");
//        }
//        communityService.createCommunity(community);
//        return new Result(200, "删除成功");
    }

    //设置精华帖
    @CrossOrigin
    @PostMapping(value = "/api/setdigest")
    @ResponseBody
    public Result setdigest(@RequestBody Map<String, String> params) {
        String postid = params.get("postid").toString();
        Integer i = Integer.valueOf(postid);
        Post post = postService.getByPostid(i);
        post.setIsdigest(1);

        try{
            postService.Post(post);
            return new Result(200, "设置精华帖成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }

    //取消设置精华帖
    @CrossOrigin
    @PostMapping(value = "/api/canceldigest")
    @ResponseBody
    public Result canceldigest(@RequestBody Map<String, String> params) {
        String postid = params.get("postid").toString();
        Integer i = Integer.valueOf(postid);
        Post post = postService.getByPostid(i);
        post.setIsdigest(0);

        try{
            postService.Post(post);
            return new Result(200, "取消设置精华帖成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }
}
