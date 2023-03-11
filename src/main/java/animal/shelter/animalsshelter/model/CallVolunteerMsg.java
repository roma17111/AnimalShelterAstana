package animal.shelter.animalsshelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "call_volunteer_msg")
public class CallVolunteerMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_id")
    private long id;
    private long chatID;
    private long stateId;
    private String numberPhone;
    private String email;
    @Column(name = "msg_date")
    private Timestamp msgDate;

    @Column(name = "msg_text")
    private String msgText;

    public CallVolunteerMsg(long stateId) {
        this.stateId = stateId;
    }

    @Override
    public String toString() {
        return  "Вопрос от пользователя: \n" +
                "id:" + id + "\n" +
                "chatID: " + chatID + "\n" +
                "Номер телефона: " + numberPhone + "\n" +
                "email: " + email + "\n" +
                "Дата:" + msgDate + "\n" +
                "Сообщение: \n" + msgText + "\n";
    }
}
