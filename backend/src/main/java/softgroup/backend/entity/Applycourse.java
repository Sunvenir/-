package softgroup.backend.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "my_courseapply")
@Data
public class Applycourse {//加入课程的申请
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Integer applyid;                       //申请加入课程的主键，自增，只用于数据库中

    //申请人的id
    @Column(name = "apply_userid")
    private String userid;

    @Column(name = "apply_username")
    private String username;

    //该课程的id
    @Column(name = "apply_courseid")
    private Integer courseid;

    //该课程的老师id
    @Column(name = "apply_teacherid")
    private String teacherid;

    public Applycourse(){}

    public Applycourse(String userid, Integer courseid, String teacherid,String username){
        this.userid = userid;
        this.courseid = courseid;
        this.teacherid = teacherid;
        this.username = username;
    }

}
