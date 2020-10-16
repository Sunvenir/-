package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.controller.ApplyCommunityController;
import softgroup.backend.entity.*;

import java.util.List;

@Repository
public interface ApplycommunityRepository extends JpaRepository<Applycommunity,Integer> {

    List<Applycommunity> findByApplyidNotNull();

    List<Applycommunity> findByUserid(String userid);

    List<Applycommunity> findByCommunityid(Integer communityid);

    Applycommunity findByUseridAndCommunityid(String userid,Integer communityid);


    @Transactional
    void deleteByUserid(String userid);

    @Transactional
    void deleteByUseridAndCommunityid(String userid,Integer communityid);

    @Transactional
    void deleteByApplyid(Integer applyid);
}
