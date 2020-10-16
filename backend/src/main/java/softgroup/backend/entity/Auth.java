package softgroup.backend.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "my_authentications")
@Data
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Integer authid;                       //认证的主键，自增，只用于数据库中

    //申请人的id
    @Column(name = "auth_userid")
    private String userid;

    @Column(name = "auth_username")
    private  String username;

    //申请信息
    @Column(name = "text")
    private String authtext;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    public Auth(){}

    public Auth(String id, String username, String text, String email, String phone){
        userid = id;
        authtext = text;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }
}
