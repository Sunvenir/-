package softgroup.backend.controller;

import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.*;
import softgroup.backend.service.CourseService;
import softgroup.backend.service.Course_studentService;
import softgroup.backend.service.VideoService;
import softgroup.backend.service.Video_studentService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class WatchtimeController {
    @Resource
    Video_studentService video_studentService;

    @Resource
    VideoService videoService;

    @Resource
    Course_studentService course_studentService;

    @Resource
    CourseService courseService;

    @CrossOrigin
    @PostMapping(value = "/api/updatetime")
    @ResponseBody
    public Result updatetime(@RequestBody Map<String, String> params){
        Video_student video_student = video_studentService.getByVideoandUser(Integer.valueOf(params.get("videoid")), params.get("userid"));
        Video video =  videoService.getById(Integer.valueOf(params.get("videoid")));
        Course course = video.getCourse();
        Integer courseid = course.getCourseid();
        Integer coursetime = course.getCoursetime();
        Course_student course_student = course_studentService.getByCourseidAndUserid(params.get("userid"), courseid);
        course_student.setSumtime(coursetime);
        Integer sumtime = videoService.getById(Integer.valueOf(params.get("videoid"))).getVideotime();
        if(video_student == null)
        {
            video_studentService.update(new Video_student(Integer.valueOf(params.get("videoid")),
                    params.get("userid"),Integer.valueOf(params.get("time")), sumtime));
            course_student.setWatchtime(Integer.valueOf(params.get("time")));
            course_studentService.save(course_student);
            return new Result(200, "花q");
        }
        Integer time = Integer.valueOf(params.get("time"));
        //fuck
        //Video video = videoService.getById(Integer.valueOf(params.get("videoid")));
        time += video_student.getWatchtime();
        Integer temp_time = course_student.getWatchtime();
        temp_time -= video_student.getWatchtime();
        if(time.compareTo(video_student.getSumtime())>=0){
            video_student.setWatchtime(video_student.getSumtime());
            temp_time += video_student.getSumtime();
        }
        else{
            temp_time += time;
            video_student.setWatchtime(time);
        }
        //course_student.setWatchtime(temp_time);
        video_studentService.update(video_student);
        course_studentService.save(course_student);
        return new Result(200, "更新成功");
    }

    //返回课程学习进度
    @CrossOrigin
    @PostMapping(value = "/api/getcoursewatchtime")
    @ResponseBody
    public Result getcoursewatchtime(@RequestBody Map<String, String> params){
        Course_student course_student = course_studentService.getByCourseidAndUserid(params.get("userid"),Integer.valueOf(params.get("courseid")));
        Course course = courseService.getById(Integer.valueOf(params.get("courseid")));
        List<Video> videos =  videoService.getByCourse(course);
        Integer sumtime = 0;
        for(int i = 0; i<videos.size(); i++){
            if(video_studentService.getByVideoandUser(videos.get(i).getVideoId(), params.get("userid")) == null)
                continue;
            Video_student video_student = video_studentService.getByVideoandUser(videos.get(i).getVideoId(), params.get("userid"));
            Integer watch =  video_student.getWatchtime();
            Integer sum = video_student.getSumtime();
            if(sum - watch <= 3000){
                video_student.setWatchtime(sum);
                video_studentService.update(video_student);
            }
            sumtime += video_studentService.getByVideoandUser(videos.get(i).getVideoId(), params.get("userid")).getWatchtime();
        }
        course_student.setWatchtime(sumtime);
        return new Result(200,"返回成功",course_student);
    }
}
