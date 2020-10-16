package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.*;

import java.util.List;

public interface Community_studentRepository extends JpaRepository<Community_student, Integer> {
    List<Community_student> findByCommunityId(Integer Communityid);

    List<Community_student> findByStudentId(String studentid);

    Community_student findByStudentIdAndAndCommunityId(String studentid,Integer communityid);

    @Transactional
    void deleteByStudentIdAndCommunityId(String studentid,Integer communityid);
}
