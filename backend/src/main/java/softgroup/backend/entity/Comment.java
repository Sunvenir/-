package softgroup.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "my_comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id")
    private Integer comid;                       //评论唯一标识,主键

    @Column(name = "com_content")
    private String content;                 //评论内容

    @Column(name = "com_time")
    private Date comtime;                   //评论时间

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "com_user_id",referencedColumnName = "user_cnt",foreignKey = @ForeignKey(name = "commentposter"))
    @JsonBackReference
    private User commentuser;               //评论发起人

    @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "com_post_id",referencedColumnName = "post_id",foreignKey = @ForeignKey(name = "comment_post"))
    @JsonBackReference
    private Post commonPost;            //评论的帖子

    @Column(name = "com_likes")
    private Integer likes = 0;                    //点赞数，初始为0

    public void like(){                         //点赞
        this.likes++;
    }
}
