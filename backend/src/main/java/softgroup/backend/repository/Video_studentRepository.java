package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.Video_student;

@Repository
public interface Video_studentRepository extends JpaRepository<Video_student, Integer> {

    Video_student findByVideoidAndUserid(Integer videoid, String userid);

    @Transactional
    void deleteByVideoid(Integer vedioid);


}
