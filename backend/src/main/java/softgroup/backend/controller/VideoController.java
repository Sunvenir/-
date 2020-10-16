package softgroup.backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softgroup.backend.entity.Result;
import softgroup.backend.entity.Video;
import softgroup.backend.service.VideoService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class VideoController {
    @Resource
    VideoService videoService;

    @CrossOrigin
    @PostMapping("/api/renew")
    public Result videoUpload(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> paras) {
        if (file == null || file.isEmpty()) {
            return new Result(400, "似乎是空文件");
        }

        String url = videoService.uploadVideo(file, paras);
        if (url != null) {
            return new Result(200, url);
        }
        return new Result(400, "上传失败");
    }

    @CrossOrigin
    @GetMapping("/api/video/detail")
    public Result<Video> videoWatch(@RequestBody Map<String, String> paras) {
        String videoId = paras.get("videoId");
        Video video = videoService.getById(Integer.parseInt(videoId));
        if (video == null) {
            return new Result<>(404, "Not Found", null);
        }
        return new Result<>(200, "", video);
    }

    @CrossOrigin
    @GetMapping("/api/courseVideo")
    public Result<List<Video>> videoByCourse(@RequestBody Map<String, String> paras) {
        return new Result<>(200, "", videoService.getByCourseName(paras.get("courseName")));
    }
}