package softgroup.backend.repository;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.Post;
import softgroup.backend.entity.User;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer>, JpaSpecificationExecutor<User> {
    Post findByPostid(Integer id);

    @Transactional
    void deleteByPostid(Integer postid);

}
