package softgroup.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import softgroup.backend.entity.Post;

/**
 * 帖子实体类
 */

//@Entity
//@Table(name = "my_postsinfo")
@Data
public class Postinfo {
    Integer postid;
    String head;
    String text;
    String time;
    Integer posterid;
    String postername;
    String posteravatar;
    String userid;
    int istop;
    int isdigest;
    int comments_number;
    int likes;
    //Integer communityid;
    //String communityname;
}
