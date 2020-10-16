package softgroup.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 */

@Entity
@Table(name = "my_users")
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cnt")   //对应表中字段
    private Integer cnt;

    @Column(name = "user_id")   //对应表中字段
    private String userid;     //学号

    @Column(name = "user_name")   //对应表中字段
    private String username;    //用户名

    @Column(name = "user_pwd")   //对应表中字段
    private String password;    //密码

    @Column(name = "user_type")   //对应表中字段
    private Integer usertype;    //用户类型(0为学生/1为老师)

    @Column(name = "user_email")   //对应表中字段
    private String email;       //邮箱

    @Column(name = "user_phone")   //对应表中字段
    private String phone;       //手机号

    @Column(name = "user_avatar",columnDefinition = "mediumtext")   //对应表中字段
    private String avatar;      //头像

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "User_Community",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "Community_Id"))
    private Set<Community> communities = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "commentuser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();



}
