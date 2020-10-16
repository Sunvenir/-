package softgroup.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.Result;
import softgroup.backend.entity.User;
import softgroup.backend.service.UserService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class ChpasswdController {

    @Resource
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/changepassword")
    @ResponseBody
    public Result chpasswd(@RequestBody Map<String, String> params) {
        User user = userService.get(params.get("id").toString(), params.get("password").toString());
        if(user == null) {
            return new Result(400, "用户名或密码错误");
        }
        user.setPassword(params.get("newpassword").toString());
        userService.addUser(user);
        return new Result(200, "密码修改成功");
    }

}
