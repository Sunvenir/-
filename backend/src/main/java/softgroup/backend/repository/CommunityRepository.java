package softgroup.backend.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import softgroup.backend.entity.Community;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, String> {
    Community findById(Integer id);

    Community findByName(String name);

    //List<Community> findCommunitiesByTeacherId(String teacherId);

}
