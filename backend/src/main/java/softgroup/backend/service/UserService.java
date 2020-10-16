package softgroup.backend.service;

import org.springframework.stereotype.Service;
import softgroup.backend.entity.User;
import softgroup.backend.repository.UserRepository;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserRepository userRepository;

    public boolean isExisted(String id) {
        return userRepository.findByUserid(id) != null;
    }

    public User getByName(String username) {
        return userRepository.findByUsername(username);
    }

    public User getById(String id) { return userRepository.findByUserid(id); }

    public User get(String id, String password) {
        return userRepository.findByUseridAndPassword(id, password);
    }

    //添加用户
    public void addUser(User user) {
        userRepository.save(user);
    }
}
