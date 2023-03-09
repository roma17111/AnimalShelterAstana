package animal.shelter.animalsshelter.controllers.contexts.messagecontext;

import animal.shelter.animalsshelter.controllers.TelegramBotStart;
import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import lombok.Data;

@Data
public class MessageContext {

    private final TelegramBotStart bot;
    private final CallVolunteerMsg msg;
    private final String input;

    public static MessageContext of(TelegramBotStart bot, CallVolunteerMsg msg, String text) {
        return new MessageContext(bot, msg, text);
    }

    private MessageContext(TelegramBotStart bot, CallVolunteerMsg msg, String input) {
        this.bot = bot;
        this.msg = msg;
        this.input = input;
    }

}
