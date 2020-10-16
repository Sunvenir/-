package softgroup.backend.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Vedio_student")
@Data
public class Video_student {//记录用户观看某个视频的时长
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vedio_student_id")
    private Integer id;

    @Column(name = "video_id")
    private Integer videoid;

    @Column(name = "user_id")
    private String userid;

    @Column(name = "watch_time")//该视频用户观看时长
    private Integer watchtime;

    @Column(name = "sum_time")//该视频总时长
    private Integer sumtime;

    public Video_student(){}

    public Video_student(Integer videoid, String userid, Integer watchtime, Integer sumtime){
        this.videoid= videoid;
        this.userid = userid;
        this.sumtime = sumtime;
        this.watchtime = watchtime;
    }

}
