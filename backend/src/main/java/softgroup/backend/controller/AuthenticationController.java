package softgroup.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import softgroup.backend.entity.Auth;
import softgroup.backend.entity.Result;
import softgroup.backend.entity.User;
import softgroup.backend.service.AuthenticationService;
import softgroup.backend.service.UserService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class AuthenticationController {
    @Resource
    AuthenticationService authenticationService;

    @Resource
    UserService userService;

    //用户提交申请
    @CrossOrigin
    @PostMapping(value = "/api/authsubmit")
    @ResponseBody
    public Result submit(@RequestBody Map<String, String> params) {
        if(authenticationService.getByUserId(params.get("userid")) == null)
        {
            User user = userService.getById(params.get("userid"));
            Auth auth = new Auth(params.get("userid"), user.getUsername(),
                    params.get("text"), user.getEmail(), user.getPhone());
            authenticationService.submit(auth);
            return new Result(200, "申请成功");
        }
        else
        {
            return new Result(400, "您已经提交过审核了！");
        }
    }

    @CrossOrigin
    @PostMapping(value = "/api/getauthlist")
    @ResponseBody
    public Result get_authlist(@RequestBody Map<String, String> params) {
        return new Result(200, "列表返回成功！", authenticationService.getList());
    }

    //管理员通过审核
    @CrossOrigin
    @PostMapping(value = "/api/authaccept")
    @ResponseBody
    public Result authenticate(@RequestBody Map<String, String> params) {
        User user = userService.getById(params.get("userid").toString());
        if(authenticationService.getByUserId(params.get("userid").toString()) == null)
            return new Result(400, "错误，没有这个申请");
        user.setUsertype(1);
        userService.addUser(user);
        try{
            authenticationService.deletebyUserid(params.get("userid").toString());
        }
        catch (Exception e){
            System.out.println(e.toString());
            return new Result(400,"审核失败");
        }
        return new Result(200, "审核成功！");
    }

    //管理员拒绝审核
    @CrossOrigin
    @PostMapping(value = "/api/authrefuse")
    @ResponseBody
    public Result refuse(@RequestBody Map<String, String> params) {
        String userid = params.get("userid");
        if(authenticationService.getByUserId(userid) == null)
            return new Result(400, "错误，没有这个申请");
        try{
            authenticationService.deletebyUserid(userid);
        }
        catch (Exception e){
            return new Result(400,"审核失败");
        }
        return new Result(200, "拒绝申请成功");
    }
}
