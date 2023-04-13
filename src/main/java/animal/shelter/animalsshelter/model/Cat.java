package animal.shelter.animalsshelter.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cat {
    public enum CatType {
        KITTEN,
        CAT_CAT,
        DISABLED_CAT
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private long id;

    private long chatId;

    private int stateId;
    private String name;
    private String age;
    private String breed;
    @Column(name = "cat_type")
    @Enumerated(EnumType.STRING)
    private CatType catType;
    private String description;
    private byte[] photo;

    @Override
    public String toString() {
        return "Кошка: " + id +'\n' +
                "Имя: " + name + '\n' +
                "Возраст: " + age + '\n' +
                "Порода: " + breed + '\n' +
                "Тип: " + catType +'\n' +
                "Описание " + description+'\n';
    }
}
