package softgroup.backend.service;

import org.springframework.stereotype.Service;
import softgroup.backend.entity.Post;
import softgroup.backend.entity.Result;
import softgroup.backend.repository.PostRepository;

import javax.annotation.Resource;

@Service
public class PostService {
    @Resource
    PostRepository postRepository;

    // 发帖普通帖子
    public void Post(Post post){
        postRepository.save(post);
    }
    public Post getByPostid(Integer id) {
        return postRepository.findByPostid(id);
    }


}
