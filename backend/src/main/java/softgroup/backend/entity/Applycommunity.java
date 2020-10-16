package softgroup.backend.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "my_communityapply")
@Data
public class Applycommunity {//加入社区的申请
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Integer applyid;                       //申请加入课程的主键，自增，只用于数据库中

    //申请人的id
    @Column(name = "apply_userid")
    private String userid;

    //申请人姓名
    @Column(name = "apply_username")
    private String username;

    //该社区的id
    @Column(name = "apply_communityid")
    private Integer communityid;

    public Applycommunity(){}

    public Applycommunity(String userid, Integer communityid, String username){
        this.userid = userid;
        this.communityid = communityid;
        this.username = username;
    }

}
