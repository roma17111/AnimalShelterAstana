package animal.shelter.animalsshelter.services;

import com.vdurmont.emoji.EmojiParser;
import lombok.Data;

@Data
public class StartMenu {

    public String sayHello(){
        String hello = EmojiParser.parseToUnicode("Общаясь со мной, можно узнать:\n" +
                Emoji.PURPLE_HEART + "Миссию и историю приюта\n" +
                Emoji.GREEN_HEART + "Наши ценности и принципы работы\n" +
                Emoji.ORANGE_HEART + "Общую информацию для желающих взять питомца из приюта\n" +
                Emoji.BLUE_HEART + "Выбрать себе питомца\n" +
                Emoji.RED_HEART + "Получать поддержку и консультации волонтёров\n" +
                Emoji.YELLOW_HEART + "Узнать больше о способах помощи приюту.\n\n" +
                Emoji.GROWING_HEART + "Давайте знакомиться!");
        return hello;
    }


}
