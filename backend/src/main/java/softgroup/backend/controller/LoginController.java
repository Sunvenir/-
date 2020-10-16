package softgroup.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.Result;
import softgroup.backend.entity.User;
import softgroup.backend.service.UserService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class LoginController {

    @Resource
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> params) {

        User user = userService.get(params.get("id").toString(), params.get("password").toString());
        if(user == null) {
            return new Result(400, "用户名或密码错误");
        }
        else return new Result<User>(200, "登录成功", user);
    }
}
