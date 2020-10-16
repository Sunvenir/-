
package softgroup.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 社区实体类
 */

@Entity
@Table(name = "my_communities")
@Data
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Integer id;     //社区Id

    @Column(name = "community_name", nullable = false)
    private String name;    //社区名

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id",referencedColumnName = "user_cnt")
    @JsonIgnoreProperties(value = {"myCommunities"})
    private User creator;                        //社区创建者

//    @OneToOne(targetEntity = Course.class)
//    @JoinColumn(name = "class_id",referencedColumnName = "course_id")
//    @JsonIgnoreProperties(value = {"myCommunities"})
//    private Course course;

    @Column(name = "community_rules")
    private String rules;       //社区规则

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> post_list = new ArrayList<>();                           //帖子组

    @ManyToMany(mappedBy = "communities")
    private Set<User> users = new HashSet<>();

    public boolean deletePost(String postid){
        for(int i=0;i<post_list.size();i++) {
            if(post_list.get(i).getPostid().equals(postid)) {
                post_list.remove(i);
                return true;
            }
        }
        return false;
    }
    public List<Post> showList(){
        return post_list;
    }
}

