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
//@Table(name = "my_commentinfo")
@Data
public class Commentinfo {
    Integer comid;
    String content;
    String comtime;
    Integer commentuserid;
    String commentusername;
    String commentuseravatar;
    String userid;
    int likes;
    //Integer communityid;
    //String communityname;
}
