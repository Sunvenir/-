package softgroup.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

/**
 * 帖子实体类
 */

@Entity
@Table(name = "my_posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postid;                       //帖子唯一标识,主键

    @Column(name = "post_head")
    private String head;                         //标题

    @Column(name = "post_text")
    private String text;                         //内容

    @Column(name = "post_likes")
    private Integer likes = 0;                    //点赞数，初始为0

    @Column(name = "post_time")
    private Date time;                          //时间

    @Column(name = "is_digest_post")
    private int isdigest = 0;          //判断是否为精华帖


    @Column(name = "is_top")
    private int istop = 0;             //判断是否置顶

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id",referencedColumnName = "user_cnt",foreignKey = @ForeignKey(name = "poster"))
    @JsonBackReference
    private User poster;                        //帖子发布者

    @ManyToOne(targetEntity = Community.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_community_id",referencedColumnName = "community_id",foreignKey = @ForeignKey(name = "post_community"))
    @JsonBackReference
    private Community community;                //帖子所属社区

    @OneToMany(mappedBy = "commonPost",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<Comment>(); //帖子评论

//    @Column(name = "comments_number")
//    private int comments_number = 0;
    public void like(){                         //点赞
        this.likes++;
    }
    public void topPost(){                      //置顶
        this.isdigest = 1;
    }
    public List<Comment> showComment(){
        return comments;
    }
}
