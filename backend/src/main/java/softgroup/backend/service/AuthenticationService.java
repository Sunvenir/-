package softgroup.backend.service;

import org.springframework.stereotype.Service;
import softgroup.backend.entity.Auth;
import softgroup.backend.repository.AuthenticationRepository;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AuthenticationService {
    @Resource
    AuthenticationRepository authenticationRepository;

    public Auth getByUserId(String userid) {
        return authenticationRepository.findByUserid(userid);
    }

    public void submit(Auth auth) { authenticationRepository.save(auth); } //提交认证申请

    public List<Auth> getList(){return authenticationRepository.findByAuthidNotNull();}

    public void deletebyUserid(String userid){
        authenticationRepository.deleteByUserid(userid);
    }
}
