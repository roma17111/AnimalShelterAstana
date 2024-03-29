package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer id;

    private int stateId;

    private long chatId;

    @Column(name = "msg_date")
    private Timestamp msgDate;

    @Column(name = "diet")
    private String diet;

    @Column(name = "general_health")
    private String generalHealth;

    @Column(name = "behavior_change")
    private String behaviorChange;

    private byte[] photo;

    @OneToOne
    @JoinColumn(name = "dog_id", referencedColumnName = "dog_id")
    private Dog dog;

    @OneToOne
    @JoinColumn(name = "cat_id", referencedColumnName = "cat_id")
    private Cat cat;

    private String userInfo;

    @Override
    public String toString() {
        return "id: " + id +"\n" +
                "Дата " +"\n" + msgDate + "\n" +
                "Диета " +"\n" + diet + "\n" +
                "Состояние здоровья " + "\n" +generalHealth + "\n" +
                "Поведение питомца " +"\n" + behaviorChange + "\n" +
                "Информация о пользователе " +"\n" + userInfo+"\n";
    }
}

