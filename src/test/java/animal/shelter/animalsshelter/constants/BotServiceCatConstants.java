package animal.shelter.animalsshelter.constants;


import animal.shelter.animalsshelter.model.Cat;
import animal.shelter.animalsshelter.model.Dog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class BotServiceCatConstants extends Exception {

    public static byte[] photoTest(BufferedImage bImage) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
        byte [] data = bos.toByteArray();
        return data;
    }
    public static Integer CAT_ID_DEFAULT;
    public static String CAT_AGE_DEFAULT = "5";
    public static String CAT_AGE_CORRECT = "3";
    public static String CAT_AGE_EMPTY = "";
    public static String CAT_AGE_ONLY_SPACES = " ";
    public static final String NICKNAME_EMPTY = "";
    public static final String CAT_BREED_CORRECT = "Дворовой";
    public static final String CAT_BREED_DEFAULT = "Порода";
    public static final String CAT_BREED_ONLY_SPACES = "  ";
    public static final String CAT_BREED_EMPTY = "";
    public static final String NICKNAME_DEFAULT = "Кличка";
    public static final String NICKNAME_ONLY_SPACES = "  ";
    public static final String NICKNAME_ILLEGAL_CHARACTERS = "Шарик123";
    public static final String NICKNAME_CORRECT = "Шарик";

    public static final String CAT_DESCRIPTION_EMPTY = "";
    public static final String CAT_DESCRIPTION_DEFAULT = "Текстовое описание";
    public static final String CAT_DESCRIPTION_ONLY_SPACES = "  ";
    public static final String CAT_DESCRIPTION_CORRECT = "Тестовый текст";

    public static final Cat.CatType CAT_TYPE_DEFAULT = Cat.CatType.CAT_CAT;

    public static final byte[] CAT_PHOTO_DEFAULT;
    static {
        try {
            CAT_PHOTO_DEFAULT = photoTest(ImageIO.read(new File("src/main/resources/images/dog/img.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static final byte[] CAT_PHOTO_EMPTY = null;

}



