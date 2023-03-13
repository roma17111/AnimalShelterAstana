package animal.shelter.animalsshelter.model;

import lombok.Data;

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
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "introduction_rules")
    private String introductionRules;

    @Column(name = "required_documents")
    private String requiredDocuments;

    @Column(name = "transportation_recommendations")
    private String transportationRecommendations;

    @Column(name = "dog_type")
    @Enumerated(EnumType.STRING)
    private DogType dogType;

    @Column(name = "home_arrangement_recommendations")
    private String homeArrangementRecommendations;

    @Column(name = "primary_communication_tips")
    private String primaryCommunicationTips;

    @Column(name = "recommended_kynologists")
    private String recommendedKynologists;

    @Column(name = "refusal_reasons")
    private String refusalReasons;

    @Column(name = "dog_photo")
    private byte[] dogPhoto;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "state_id")
    private int stateId;

    @Column(name = "chat_id")
    private long chatId;
}
