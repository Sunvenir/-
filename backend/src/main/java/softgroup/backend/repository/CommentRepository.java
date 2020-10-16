package softgroup.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import softgroup.backend.entity.Comment;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import softgroup.backend.entity.Post;

import java.util.List;


@Repository

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Comment findByComid(Integer id);

    List<Comment> findAllByCommonPost(Integer commonPost);

    @Transactional
    void deleteByComid(Integer comid);
}
