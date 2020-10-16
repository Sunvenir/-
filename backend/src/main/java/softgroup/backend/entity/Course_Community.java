package softgroup.backend.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "course_community")
@Data
public class Course_Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_community_id")
    private Integer coursecommunityid;    //主键，课程id

    @Column(name = "course_id")
    private Integer courseid;

    @Column(name = "community_id")
    private Integer communityid;
}
