package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.controller.ApplyCourseController;
import softgroup.backend.entity.Applycourse;
import softgroup.backend.entity.Auth;

import java.util.List;

@Repository
public interface ApplycourseRepository extends JpaRepository<Applycourse,Integer> {

    List<Applycourse> findByApplyidNotNull();

    List<Applycourse> findByUserid(String userid);

    List<Applycourse> findByTeacherid(String teacherid);

    List<Applycourse> findByCourseid(Integer courseid);

    Applycourse findByUseridAndCourseid(String userid,Integer courseid);


    @Transactional
    void deleteByUserid(String userid);

    @Transactional
    void deleteByUseridAndCourseid(String userid,Integer courseid);

    @Transactional
    void deleteByApplyid(Integer applyid);
}
