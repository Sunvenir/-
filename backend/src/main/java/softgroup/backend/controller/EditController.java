package softgroup.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softgroup.backend.entity.Result;
import softgroup.backend.entity.User;
import softgroup.backend.service.UserService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class EditController {
    @Resource
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/edit")
    @ResponseBody
    Result Edit(@RequestBody Map<String, String> params){
        User new_user = userService.getById(params.get("id").toString());
        //new_user.setUsername(params.get("username").toString());
        new_user.setAvatar(params.get("avatar").toString());
        //new_user.setEmail(params.get("email").toString());
        //new_user.setPhone(params.get("phone").toString());

        userService.addUser(new_user);
        return new Result(200,"修改成功");


    }
}
