package softgroup.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "my_course")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseid;    //主键，课程id

    @Column(name = "course_name")
    private String coursename;   //课程名称

    @Column(name = "course_school")
    private String courseschool;   //课程所属学校

    @Column(name = "course_type")
    private String coursetype;   //课程类别

    @Column(name = "course_participant")
    private Integer participant;   //课程参与人数

    @Column(name = "course_introduction")
    private String courseintro;   //课程简介

    @Column(name = "course_cover",columnDefinition = "mediumtext")
    private String coursecover;   //课程封面

    @Column(name = "teacher_name")
    private  String teachername;

    @Column(name = "course_time")
    private Integer coursetime; //课程总时长

    @JsonIgnore
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Video> gallery = new HashSet<>();

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "course_user"))
    private User teacher;

    public Course(){this.coursetime = 0;}

    public void addtime(Integer time){
        this.coursetime += time;
    }
}
