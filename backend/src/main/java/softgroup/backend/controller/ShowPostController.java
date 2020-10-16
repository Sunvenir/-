package softgroup.backend.controller;



import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.*;
import softgroup.backend.service.*;
import softgroup.backend.service.CommunityService;
import softgroup.backend.repository.CommunityRepository;
import softgroup.backend.service.PostService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ShowPostController {
    @Resource
    CommunityService communityService;

    @Resource
    Course_CommunityService course_communityService;

    @CrossOrigin
    @PostMapping(value = "/api/showpost")
    @ResponseBody
    public Result show(@RequestBody Map<String, String> params) throws ParseException {
        Integer courseid = Integer.valueOf(params.get("courseid").toString());
        Course_Community course_community = course_communityService.findByCourseid(courseid);
        Integer communityid= course_community.getCommunityid();


        Integer i = communityid;
        Integer sort_style =  Integer.valueOf(params.get("sort_style".toString()));
        Integer list_type = Integer.valueOf(params.get("list_type".toString()));
        Community community = communityService.getById(i);
        List<Post> post_list= community.showList();
        List<Postinfo> info_list= new ArrayList<Postinfo>();
        List<Postinfo> temp_list= new ArrayList<Postinfo>();

        for( int j= 0;j<post_list.size();j++){
            Postinfo postinfo =  new Postinfo();
            postinfo.setHead(post_list.get(j).getHead());
            postinfo.setText( post_list.get(j).getText());
            postinfo.setPostid( post_list.get(j).getPostid());
            postinfo.setPosterid( post_list.get(j).getPoster().getCnt());
            postinfo.setUserid( post_list.get(j).getPoster().getUserid().toString());
            postinfo.setPostername( post_list.get(j).getPoster().getUsername());
            postinfo.setTime( post_list.get(j).getTime().toString() );
            postinfo.setIstop(post_list.get(j).getIstop());
            postinfo.setIsdigest(post_list.get(j).getIsdigest());
            postinfo.setPosteravatar(post_list.get(j).getPoster().getAvatar());
            List <Comment> comment = post_list.get(j).showComment();
            int n = comment.size();
            if(comment.isEmpty())
               postinfo.setComments_number(0);
            else
            postinfo.setComments_number(comment.size());
            postinfo.setLikes(post_list.get(j).getLikes());
            //postinfo.setCommunityid(post_list.get(j).getCommunity().getId());
            //postinfo.setCommunityname( post_list.get(j).getCommunity().getName());
            info_list.add(postinfo);
        }
        for( int j= info_list.size()-1;j>-1;j--) {
            if(info_list.get(j).getIstop()==1) {
                temp_list.add(info_list.get(j));
                info_list.remove(j);
            }
        }
        if(sort_style==1) {                                             //按时间排序
            info_list.sort(Comparator.comparing(Postinfo::getTime).reversed());
            temp_list.sort(Comparator.comparing(Postinfo::getTime).reversed());
        }
        else if(sort_style==2){                                         //按点赞排序
            info_list.sort(Comparator.comparing(Postinfo::getLikes).thenComparing(Postinfo::getTime).reversed());
            temp_list.sort(Comparator.comparing(Postinfo::getLikes).thenComparing(Postinfo::getTime).reversed());
        }
        else if(sort_style==3){                                         //按评论数排序
            info_list.sort(Comparator.comparing(Postinfo::getComments_number).thenComparing(Postinfo::getTime).reversed());
            temp_list.sort(Comparator.comparing(Postinfo::getComments_number).thenComparing(Postinfo::getTime).reversed());
        }
        for( int j= 0;j<info_list.size();j++) {

            temp_list.add(info_list.get(j));
        }
        List<Postinfo> top_list= new ArrayList<Postinfo>();
        for( int j= 0;j<temp_list.size();j++) {
            if(temp_list.get(j).getIstop()==1)
            top_list.add(temp_list.get(j));
        }
        List<Postinfo> digest_list= new ArrayList<Postinfo>();
        for( int j= 0;j<temp_list.size();j++) {
            if(temp_list.get(j).getIsdigest()==1)
                digest_list.add(temp_list.get(j));
        }
        try{
            if(list_type==1) {
                return new Result(200, "发送成功", temp_list);
            }
            else if(list_type==2){
                return new Result(200, "发送成功", top_list);
            }
            else if(list_type==3){
                return new Result(200, "发送成功", digest_list);
            }
            else
                return new Result(200, "发送成功", temp_list);

        }
        catch(Exception e){
            return new Result(400, e.toString());
        }
    }
}


