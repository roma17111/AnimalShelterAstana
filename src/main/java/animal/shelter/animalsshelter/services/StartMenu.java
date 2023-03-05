package animal.shelter.animalsshelter.services;

import com.vdurmont.emoji.EmojiParser;
import lombok.Data;

import javax.ws.rs.HEAD;

@Data
public class StartMenu {

    public String sayHello() {
        String hello = EmojiParser.parseToUnicode("Общаясь со мной, можно узнать:\n" +
                Emoji.PURPLE_HEART + " Миссию и историю приюта\n" +
                Emoji.GREEN_HEART + " Наши ценности и принципы работы\n" +
                Emoji.ORANGE_HEART + " Общую информацию для желающих взять питомца из приюта\n" +
                Emoji.BLUE_HEART + " Выбрать себе питомца\n" +
                Emoji.RED_HEART + " Получать поддержку и консультации волонтёров\n" +
                Emoji.YELLOW_HEART + " Узнать больше о способах помощи приюту.\n\n" +
                Emoji.GROWING_HEART + " Давайте знакомиться!");
        return hello;
    }

    public String getInfoAboutShelter() {
        String info = EmojiParser.parseToUnicode(Emoji.LOVE_YOU_HAND + " Миссия: \n" +
                "Мы любим людей и животных, поэтому помогаем друг другу " +
                "и обучаем людей, как заботиться о питомцах профессионально и с любовью!\n\n" +
                Emoji.OK_HAND + " О нас: \n" +
                Emoji.OK_HAND + " О нас:\n" +
                "Asha - это приют для собак и кошек в г. Астана. Мы работаем уже 4 года. " +
                "За это время мы приняли, вылечили и адаптировали к жизни в новых домах несколько сотен животных, " +
                "а также обучили не один десяток волонтёров, как взаимодействовать с разными животными. " +
                "У нас есть свои традиции, правила и принципы работы, при этом мы открыты к новому и стараемся стать " +
                "доступнее и открытее, используя современные технологии!\n\n"+
                Emoji.UP_POINT_HAND + " Ценности приюта: \n" +
                Emoji.CHECK_MARK + " Забота о питомце, оказавшемся в беде\n" +
                Emoji.CHECK_MARK + " Помощь в обретении нового дома каждому нашему питомцу\n" +
                Emoji.CHECK_MARK + " Сопровождение новых хозяев на первых порах и необходимая поддержка в любое время\n" +
                Emoji.CHECK_MARK + " Полное соблюдение законодательства\n" +
                Emoji.CHECK_MARK + " Поддержание и развитие профессионализма сотрудников и волонтёров приюта\n" +
                Emoji.CHECK_MARK + " Полная отчётность всем жертвователям приюта\n\n" +
                Emoji.HAND_SHAKE + " Наша команда: \n" +
                "С нами сотрудничают студенты ветеринарной Академии и неравнодушные любители животных. Мы знаем, как " +
                " вылечить и адаптировать животное к жизни в новом доме, поможем вам привыкнуть друг к другу и будем " +
                " вас вести на протяжении всего испытательного срока, чтобы ваш новый питомец был в радость новым усыновителям!\n");
        return info;
    }

    public String workTime() {
        String time = EmojiParser.parseToUnicode("Мы работаем : \n" +
                Emoji.ALARM_CLOCK + "Пн-Пт - с 09:30 до 19;30\n" +
                Emoji.ALARM_CLOCK + "Сб - с 09:30 до 16;30\n" +
                Emoji.CROSS_MARK + "Вс и праздничные дни - не работаем\n" +
                "(время местное)");
        return time;
    }

    public String contactUs() {
        String contact = EmojiParser.parseToUnicode("С нами можно связаться:\n" +
                Emoji.PHONE_RECEIVER + " +7-708-899-78-78\n" +
                Emoji.ENVELOPE + " asha.info@astana.kz\n" +
                "Мы находимся по адресу:\n" +
                Emoji.HOUSE_WITH_GARDEN + " г. Астана, ул. К. Маркса, д.192а\n" +
                "Схема проезда по ссылке: www.address.asha.Marksa192a.kz");
        return contact;
    }

    public String toBeSafeRegulations() {
        String regulations = EmojiParser.parseToUnicode("На территории приюта ОБЯЗАТЕЛЬНО соблюдать следующие правила:^\n" +
                Emoji.DOUBLE_BANG_MARK + " На территории приюта можно находиться только в сопровождении волонтёра\n" +
                Emoji.DOUBLE_BANG_MARK + " Нельзя гладить или кормить животных без разрешения волонтёра\n" +
                Emoji.DOUBLE_BANG_MARK + " Запрещается курить, распивать спиртные напитки, ругаться и громко разговаривать\n" +
                Emoji.DOUBLE_BANG_MARK + " Фото- и видеосъёмка возможны с разрешения волонтёра\n" +
                Emoji.DOUBLE_BANG_MARK + " Запрещено дразнить и пугать животных, просовывать и бросать в клетки посторонние предметы\n" +
                Emoji.DOUBLE_BANG_MARK + " НАРУШИТЕЛИ НЕМЕДЛЕННО ВЫДВОРЯЮТСЯ С ТЕРРИТОРИИ ПРИЮТА" + Emoji.DOUBLE_BANG_MARK + " \n" +
                "Соблюдение правил сохранит ваши жизнь и здоровье, а также убережёт животных от стрессов и других" +
                " неприятных последствий для их здоровья.");
        return regulations;
    }
}