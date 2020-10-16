package softgroup.backend.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Community_student")
@Data
public class Community_student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_student_id")
    private Integer id;

    @Column(name = "community_id")
    private Integer communityId;

    @Column(name = "student_id")
    private String studentId;

}
