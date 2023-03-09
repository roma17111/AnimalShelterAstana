package animal.shelter.animalsshelter.constants;

import animal.shelter.animalsshelter.model.Dog;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.persistence.*;




public class BotServiceDogConstants extends Exception {

    public static byte[] photoTest(BufferedImage bImage) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
        byte [] data = bos.toByteArray();
        return data;
    }
    public static Integer DOG_ID_DEFAULT;

    public static final String NICKNAME_EMPTY = "";
    public static final String NICKNAME_DEFAULT = "Кличка";
    public static final String NICKNAME_ONLY_SPACES = "  ";
    public static final String NICKNAME_ILLEGAL_CHARACTERS = "Шарик123";
    public static final String NICKNAME_CORRECT = "Шарик";

    public static final String DOG_DESCRIPTION_EMPTY = "";
    public static final String DOG_DESCRIPTION_DEFAULT = "Текстовое описание";
    public static final String DOG_DESCRIPTION_ONLY_SPACES = "  ";
    public static final String DOG_DESCRIPTION_CORRECT = "Тестовый текст";

    public static final Dog.DogType DOG_TYPE_DEFAULT = Dog.DogType.ADULT;

    public static final byte[] DOG_PHOTO_DEFAULT;
    static {
        try {
            DOG_PHOTO_DEFAULT = photoTest(ImageIO.read(new File("src/main/resources/images/dog/img.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static final byte[] DOG_PHOTO_EMPTY = null;

    }


