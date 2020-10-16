package softgroup.backend.service;

import org.springframework.stereotype.Service;
import softgroup.backend.entity.Course_Community;
import softgroup.backend.repository.Course_CommunityRepository;

import javax.annotation.Resource;

@Service
public class Course_CommunityService {
    @Resource
    Course_CommunityRepository course_communityRepository;

    public void Course_Community(Course_Community course_community){
        course_communityRepository.save(course_community);
    }
    public Course_Community findByCourseid(Integer courseid){return course_communityRepository.findByCourseid(courseid);}
    public Course_Community findByCommunityid(Integer communityid){return course_communityRepository.findByCommunityid( communityid);}
}
