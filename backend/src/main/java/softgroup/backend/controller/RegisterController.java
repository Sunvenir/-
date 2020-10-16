package softgroup.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.Result;
import softgroup.backend.entity.User;
import softgroup.backend.service.UserService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class RegisterController {
    @Resource
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/register")
    @ResponseBody
    Result register(@RequestBody Map<String, String> params){
        User new_user = new User();
        new_user.setPassword(params.get("password").toString());
        new_user.setUsername(params.get("username").toString());
        new_user.setAvatar(params.get("avatar").toString());
        new_user.setEmail(params.get("email").toString());
        new_user.setPhone(params.get("phone").toString());
        new_user.setUserid(params.get("id").toString());
        new_user.setUsertype(0);

        //有重复名称
        if(userService.getById(params.get("id").toString()) != null){
            return new Result(400,"有重复学号") ;
        }
        else{
            userService.addUser(new_user);
            return new Result(200,"注册成功");
        }

    }
}
