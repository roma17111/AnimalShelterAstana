package animal.shelter.animalsshelter.controllers.contexts.messagecontext;

import animal.shelter.animalsshelter.controllers.TelegramBotStart;
import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import lombok.Data;

@Data
public class MessageContext {

    private final TelegramBotStart bot;
    private final CallVolunteerMsg msg;
    private final String input;

    /**
     * Создает новый объект MessageContext с заданными параметрами.
     *
     * @param bot объект бота TelegramBotStart
     * @param msg объект сообщения CallVolunteerMsg
     * @param text входящий текст сообщения
     * @return новый объект MessageContext
     */
    public static MessageContext of(TelegramBotStart bot, CallVolunteerMsg msg, String text) {
        return new MessageContext(bot, msg, text);
    }

    /**
     * Конструктор класса MessageContext.
     *
     * @param bot объект бота TelegramBotStart
     * @param msg объект сообщения CallVolunteerMsg
     * @param input входящий текст сообщения
     */
    private MessageContext(TelegramBotStart bot, CallVolunteerMsg msg, String input) {
        this.bot = bot;
        this.msg = msg;
        this.input = input;
    }

}
