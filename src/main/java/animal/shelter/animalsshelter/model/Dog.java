package animal.shelter.animalsshelter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dog")
public class Dog {
    public Dog() {
    }

    public enum DogType {
        PUPPY,
        ADULT,
        DISABLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Integer id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "introduction_rules", nullable = false)
    private String introductionRules;

    @Column(name = "required_documents", nullable = false)
    private String requiredDocuments;

    @Column(name = "transportation_recommendations", nullable = false)
    private String transportationRecommendations;

    @Column(name = "dog_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DogType dogType;

    @Column(name = "home_arrangement_recommendations", nullable = false)
    private String homeArrangementRecommendations;

    @Column(name = "primary_communication_tips", nullable = false)
    private String primaryCommunicationTips;

    @Column(name = "recommended_kynologists", nullable = false)
    private String recommendedKynologists;

    @Column(name = "refusal_reasons", nullable = false)
    private String refusalReasons;

    @Column(name = "dog_photo")
    private byte[] dogPhoto;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
