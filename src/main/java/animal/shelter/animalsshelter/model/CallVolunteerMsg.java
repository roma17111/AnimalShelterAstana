package animal.shelter.animalsshelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Long id;

    @Column(name = "msg_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date msgDate;

    @Column(name = "msg_text")
    private String msgText;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;
}
