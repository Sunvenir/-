package softgroup.backend.controller;

import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.*;
import softgroup.backend.repository.CommentRepository;
import softgroup.backend.service.CommentService;
import softgroup.backend.service.CommunityService;
import softgroup.backend.service.PostService;
import softgroup.backend.service.UserService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class CommentController {
    @Resource
    CommentService commentService;
    @Resource
    UserService userService;
    @Resource
    PostService postService;
    @Resource
    CommentRepository commentRepository;

    @CrossOrigin
    @PostMapping(value = "/api/comment")
    @ResponseBody
    //评论
    public Result comment(@RequestBody Map<String, String> params) throws ParseException {
        Comment new_comment = new Comment();
        String strtime = params.get("comtime").toString();                                           //时间
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = ft.parse(strtime);
        new_comment.setComtime(date);


        String userid = params.get("commentuser").toString();                                     //评论发起人
        Integer ii = Integer.valueOf(userid);
        User commentuser = userService.getById(userid);
        new_comment.setCommentuser(commentuser);

        String postid  = params.get("commonPost").toString();                                     //所属帖子
        Integer i = Integer.valueOf(postid);
        Post  post= postService.getByPostid(i);
        new_comment.setCommonPost(post);

        new_comment.setContent(params.get("content").toString());                                 //内容
        try{
            commentService.Comment(new_comment);
            return new Result(200, "发送成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }

    //删除评论
    @CrossOrigin
    @PostMapping(value = "/api/deleteComment")
    @ResponseBody
    public Result deleteComment(@RequestBody Map<String, String> params){
        String commentid = params.get("commentid").toString();
        Integer id = Integer.valueOf(commentid);
        try{
            commentRepository.deleteByComid(id);
            return new Result(200, "删除成功");

        }
        catch (Exception e) {
            return new Result(400, "删除失败！！！");
        }


    }
    //点赞评论
    @CrossOrigin
    @PostMapping(value = "/api/likecomment")
    @ResponseBody
    public Result likecomment(@RequestBody Map<String, String> params) {
        String comid = params.get("comid").toString();
        Integer i = Integer.valueOf(comid);
        Comment comment = commentService.getByComid(i);
        comment.like();

        try{
            commentService.Comment(comment);
            return new Result(200, "点赞成功");
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }

    //显示所有评论

    @CrossOrigin
    @PostMapping(value = "/api/showcomment")
    @ResponseBody
    public Result show(@RequestBody Map<String, String> params) throws ParseException {

        String postid = params.get("postid").toString();
        Integer i = Integer.valueOf(postid);
        Integer sort_style =  Integer.valueOf(params.get("sort_style".toString()));
        Post post = postService.getByPostid(i);
        List<Comment> comment_list= post.showComment();
        List<Commentinfo> info_list= new ArrayList<Commentinfo>();
        List<Commentinfo> temp_list= new ArrayList<Commentinfo>();

        for( int j= 0;j<comment_list.size();j++){
            Commentinfo commentinfo =  new Commentinfo();
            commentinfo.setContent( comment_list.get(j).getContent());
            commentinfo.setComid( comment_list.get(j).getComid());
            commentinfo.setCommentuserid( comment_list.get(j).getCommentuser().getCnt());
            commentinfo.setCommentusername( comment_list.get(j).getCommentuser().getUsername());
            commentinfo.setComtime( comment_list.get(j).getComtime().toString());
            commentinfo.setCommentuseravatar(comment_list.get(j).getCommentuser().getAvatar());
            commentinfo.setLikes(comment_list.get(j).getLikes());
            commentinfo.setUserid(comment_list.get(j).getCommentuser().getUserid());
            //postinfo.setCommunityid(post_list.get(j).getCommunity().getId());
            //postinfo.setCommunityname( post_list.get(j).getCommunity().getName());
            info_list.add(commentinfo);
        }

        if(sort_style==1) {                                             //按时间排序
            info_list.sort(Comparator.comparing(Commentinfo::getComtime).reversed());
            temp_list.sort(Comparator.comparing(Commentinfo::getComtime).reversed());
        }
        else if(sort_style==2){                                         //按点赞排序
            info_list.sort(Comparator.comparing(Commentinfo::getLikes).thenComparing(Commentinfo::getComtime).reversed());
            temp_list.sort(Comparator.comparing(Commentinfo::getLikes).thenComparing(Commentinfo::getComtime).reversed());
        }

//        SortClass sortClass = new SortClass();                //排序
//        Collections.sort(temp_list, sortClass);
//        Collections.sort(info_list, sortClass);
        for( int j= 0;j<info_list.size();j++) {

            temp_list.add(info_list.get(j));
        }
        try{
            return new Result(200, "发送成功",temp_list );
        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }
}
