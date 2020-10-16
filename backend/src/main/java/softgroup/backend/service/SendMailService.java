package softgroup.backend.service;

import org.hibernate.Criteria;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {
    private final JavaMailSender mailSender;

    public SendMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String email) throws MailException {
        long codeTime = System.currentTimeMillis();
        double randomNum = Math.sin((double)(codeTime%1000000000));
        String result = String.format("%.8f",randomNum).substring(3,9);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("密码修改邮件");
        message.setText("亲爱的flyvd住户：您本次修改密码的验证码为("+result+")，请在三分钟内重置您的密码~(^v^)~");
        message.setFrom("flyvd<flyvd_team@163.com>");
        mailSender.send(message);
    }
}
