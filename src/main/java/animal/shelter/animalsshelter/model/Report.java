package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "diet")
    private String diet;

    @Column(name = "general_health")
    private String generalHealth;

    @Column(name = "behavior_change")
    private String behaviorChange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

