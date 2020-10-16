package softgroup.backend.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "course_student")
@Data
public class Course_student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_student_id")
    private Integer id;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "watch_time")
    private Integer watchtime;

    @Column(name = "sum_time")
    private  Integer sumtime;

    public Course_student(){
        watchtime = 0;
        sumtime = 0;
    }

    public void addtime(Integer time)
    {
        watchtime += time;
    }
}
