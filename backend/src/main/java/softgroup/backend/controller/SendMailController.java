package softgroup.backend.controller;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import softgroup.backend.entity.Result;
import softgroup.backend.service.SendMailService;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class SendMailController {
    @Resource
    SendMailService sendMailService;

    @CrossOrigin
    @PostMapping(value = "/api/sendmail")
    @ResponseBody
    Result sendmail(@RequestBody Map<String, String> params) {
        String email = params.get("email").toString();
        try{
            sendMailService.send(email);
            return new Result(200, "邮件发送成功");
        }
        catch(MailException e){
            return new Result(400, e.toString());
        }
    }
}
