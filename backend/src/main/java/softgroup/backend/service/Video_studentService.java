package softgroup.backend.service;


import org.springframework.stereotype.Service;
import softgroup.backend.entity.Video_student;
import softgroup.backend.repository.Video_studentRepository;

import javax.annotation.Resource;

@Service
public class Video_studentService {
    @Resource
    Video_studentRepository video_studentRepository;

    public Video_student getByVideoandUser(Integer videoid, String userid){
        return video_studentRepository.findByVideoidAndUserid(videoid, userid);
    }

    public void update(Video_student video_student){video_studentRepository.save(video_student);}
}
