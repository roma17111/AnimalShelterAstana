package animal.shelter.animalsshelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

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
    private long stateId;
    @Column(name = "msg_date")
    private Timestamp msgDate;

    @Column(name = "msg_text")
    private String msgText;

}
