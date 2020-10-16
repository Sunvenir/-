package softgroup.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.Course;
import softgroup.backend.entity.User;
import softgroup.backend.entity.Video;
import softgroup.backend.service.CourseService;
import softgroup.backend.service.UserService;
import softgroup.backend.service.VideoService;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class VideoEntityTest {
    @Resource
    VideoService videoService;

    @Resource
    UserService userService;

    @Resource
    CourseService courseService;

    //测试添加视频
    @Test
    public void testAdd() {
        User user = new User();
        user.setUserid("173743339");
        user.setUsername("zhadfd45oziyu");
        user.setPassword("1111");
        user.setAvatar("touxiang");
        user.setEmail("111111@qq.com");
        user.setPhone("23456787654");
        userService.addUser(user);

    }

    //测试视频关联用户(注意与事务有关的测试用例的注释)
    @Test
    @Transactional  //添加事务支持
    @Rollback(value = false)    //注意:回滚默认为true,因此要添加此条
    public void testVideoRelatedToUser() {

//        Video video = new Video();
//        video.setVideoName("测试视频用例122");
//        video.setVideoInfo("这是一个视频用例");
//        video.setVideoUptime(new Date());
//        videoService.addVideo(video);
//        User user = userService.getById("17373389");
//
//        Course course = new Course();
//        course.setCoursename("测试课程");
//        courseService.addCourse(course);
//
//
//        video.setUploader(user);
//        video.setCourse(course);
//        videoService.addVideo(video);
//        videoService.addVideotoCourse(course,video);
    }
}
