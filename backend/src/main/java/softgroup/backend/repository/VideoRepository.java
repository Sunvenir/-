package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.Course;
import softgroup.backend.entity.User;
import softgroup.backend.entity.Video;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer>, JpaSpecificationExecutor<Video> {

    Video findByVideoId(Integer videoId);

    Video findByVideoName(String videoName);

    List<Video> findAllByUploader(User user);

    List<Video> findAllByCourse(Course course);

    @Transactional
    void deleteByVideoId(Integer videoId);

}
