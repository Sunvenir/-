package softgroup.backend.service;

import org.springframework.stereotype.Service;
import softgroup.backend.entity.Applycommunity;
import softgroup.backend.repository.ApplycommunityRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApplycommunityService {
    @Resource
    ApplycommunityRepository applycommunityRepository;

    public void submit(Applycommunity apply){applycommunityRepository.save(apply);}

    public List<Applycommunity> getListbycommunityid(Integer communityid){return applycommunityRepository.findByCommunityid(communityid);}


    public void deletebyUseridAndCommunityid(String userid, Integer communityid){
        applycommunityRepository.deleteByUseridAndCommunityid(userid,communityid);
    }

    public void deletebyuserid(String userid){applycommunityRepository.deleteByUserid(userid);};

    public Applycommunity getbyUseridAndCommunityid(String userid, Integer communityid){return applycommunityRepository.findByUseridAndCommunityid(userid, communityid);}

    public List <Applycommunity> getbyUserid(String userid){return applycommunityRepository.findByUserid(userid);}

}
