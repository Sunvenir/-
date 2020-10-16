package softgroup.backend.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softgroup.backend.entity.Course;
import softgroup.backend.entity.User;
import softgroup.backend.entity.Video;
import softgroup.backend.entity.Video_student;
import softgroup.backend.repository.CourseRepository;
import softgroup.backend.repository.UserRepository;
import softgroup.backend.repository.VideoRepository;
import softgroup.backend.repository.Video_studentRepository;
import softgroup.backend.util.MinioUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class VideoService {

    @Resource
    private MinioUtil minioUtil;
    @Resource
    private VideoRepository videoRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private CourseRepository courseRepository;
    @Resource
    private Video_studentRepository video_studentRepository;
    private final String bucketName = "videos";


    //添加视频
    public void addVideo(Video video) {
        videoRepository.save(video);
    }

    //删除视频
    public void deleteVideo(Integer id){videoRepository.deleteByVideoId(id);}

    //上传视频
    public String uploadVideo(MultipartFile file, Map<String, String> paras) {
        String filename = paras.get("videoName");
        String url = minioUtil.upload(bucketName, file, filename);
        addVideo(initVideo(paras, filename, url));

        return url;
    }

    //完成要存储的信息
    private Video initVideo(Map<String, String> paras, String filename, String url) {
        Video video = new Video();
        video.setVideoName(filename);
        video.setVideoUptime(new Date());
        video.setStorePath(url);
        video.setPlaySum("0");
        video.setCommentSum("0");
        video.setSupportSum("0");
        video.setUploader(userRepository.findByUserid(paras.get("teacher")));
        video.setVideotime(Integer.valueOf(paras.get("time")));

        Course course = courseRepository.findByCourseid(Integer.valueOf(paras.get("classId")));
        course.addtime(Integer.valueOf(paras.get("time")));
        video.setCourse(course);

        return video;
    }

    //null if not found
    public String getURL(Integer videoId) {
        Video video = getById(videoId);
        if (video != null) {
            return minioUtil.getUrl(bucketName, video.getVideoName());
        }
        return null;
    }

    //通过视频名称精准查找
    public Video getByName(String videoName) {
        return videoRepository.findByVideoName(videoName);
    }

    //通过视频ID精准查找
    public Video getById(Integer videoId) {
        return videoRepository.findByVideoId(videoId);
    }

    //通过上传者查找
    public List<Video> getByUploader(User user) {
        return videoRepository.findAllByUploader(user);
    }

    //通过课程查找
    public List<Video> getByCourse(Course course) {
        return videoRepository.findAllByCourse(course);
    }

    public List<Video> getByCourseName(String name) {
        Course course = courseRepository.findByCoursename(name);
        List<Video> list = getByCourse(course);
        System.out.println(list);
        return list;
    }
}
