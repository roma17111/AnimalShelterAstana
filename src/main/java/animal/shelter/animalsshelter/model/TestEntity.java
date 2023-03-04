package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "test_table")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "age")
    private Integer age;
}