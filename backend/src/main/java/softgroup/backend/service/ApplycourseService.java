package softgroup.backend.service;

import org.springframework.stereotype.Service;
import softgroup.backend.entity.Applycourse;
import softgroup.backend.repository.ApplycourseRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApplycourseService {
    @Resource
    ApplycourseRepository applycourseRepository;

    public void submit(Applycourse apply){applycourseRepository.save(apply);}

    public List<Applycourse> getListbyteacherid(String teacherid){return applycourseRepository.findByTeacherid(teacherid);}


    public List<Applycourse> getListbycourseid(Integer courseid){return applycourseRepository.findByCourseid(courseid);}


    public void deletebyUseridAndCourseid(String userid, Integer courseid){
        applycourseRepository.deleteByUseridAndCourseid(userid,courseid);
    }

    public void deletebyuserid(String userid){applycourseRepository.deleteByUserid(userid);};

    public Applycourse getbyUseridAndCourseid(String userid, Integer courseid){return applycourseRepository.findByUseridAndCourseid(userid, courseid);}

    public List <Applycourse> getbyUserid(String userid){return applycourseRepository.findByUserid(userid);}

}
