package softgroup.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 视频实体类
 */

@Entity
@Table(name = "my_videos")
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Integer videoId;    //主键

    @Column(name = "video_name")
    private String videoName;   //视频名称

    @Column(name = "video_info")
    private String videoInfo;   //视频介绍

    @Column(name = "video_uptime")
    private Date videoUptime;   //上传时间

    @Column(name = "video_type")
    private String videoType;   //视频类型

    @Column(name = "video_store_path")
    private String storePath;   //存储路径

    @Column(name = "video_play_sum")
    private String playSum;   //视频播放次数

    @Column(name = "video_comment_sum")
    private String commentSum;   //视频评论次数

    @Column(name = "video_support_sum")
    private String supportSum;   //视频赞总数

    @Column(name = "video_time")
    private Integer videotime;//视频总时长

    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "video_user_id", referencedColumnName = "user_id",foreignKey = @ForeignKey(name = "video_user"))
    private User uploader;

    @ManyToOne(targetEntity =  Course.class)
    @JoinColumn(name = "vedio_course_id", referencedColumnName = "course_id",foreignKey = @ForeignKey(name = "vedio_course"))
    private Course course;
}
