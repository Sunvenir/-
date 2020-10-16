package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softgroup.backend.entity.Course_Community;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface Course_CommunityRepository extends JpaRepository<Course_Community, Integer> {
    Course_Community findByCourseid(Integer courseid);
    Course_Community findByCommunityid(Integer communityid);
}
