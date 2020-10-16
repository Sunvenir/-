package softgroup.backend.service;
import org.springframework.stereotype.Service;
import softgroup.backend.entity.Comment;
import softgroup.backend.entity.Post;
import softgroup.backend.repository.CommentRepository;
import javax.annotation.Resource;

@Service
public class CommentService {

    @Resource
    CommentRepository commentRepository;

    // 评论
    public void Comment(Comment comment){
        commentRepository.save(comment);
    }

    public Comment getByComid(Integer id) {
        return commentRepository.findByComid(id);
    }
}
