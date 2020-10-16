package softgroup.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import softgroup.backend.entity.Course;
import softgroup.backend.entity.Post;
import softgroup.backend.entity.User;
import softgroup.backend.entity.Video;
import softgroup.backend.repository.CourseRepository;
import softgroup.backend.repository.VideoRepository;
import softgroup.backend.service.*;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class PostEntityTest {
    @Resource
    PostService postService;

    @Resource
    UserService userService;

    @Resource
    CourseRepository courseRepository;

    @Resource
    VideoRepository videoRepository;

    @Resource
    VideoService videoService;

    @Resource
    CourseService courseService;

    @Resource
    ApplycourseService applycourseService;

    @Test
    public void testPost(){
//        Course course = courseRepository.findByCourseid(3);
//        System.out.println(course == null);
//        List<Video> videos = videoService.getByCourse(course);
//        System.out.println(videos.size());
//        //videoRepository.deleteByVideoId(2);
//        for(int i = 0;i < videos.size();i++){
//            System.out.println(videos.get(i).getVideoId());
//            videoRepository.deleteByVideoId(videos.get(i).getVideoId());
//        }
//        //videoRepository.deleteByCourse(course);
//        courseRepository.deleteByCourseid(3);
//        //videoService.deleteVideo(2);

        applycourseService.deletebyUseridAndCourseid("17373389",6);

    }
}
