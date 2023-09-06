package jpabasic.jpamain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

//    @Column(name="TEAM_ID")
//    private Long teamId;

    // JPA Should be noticed about the relationship
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

}
