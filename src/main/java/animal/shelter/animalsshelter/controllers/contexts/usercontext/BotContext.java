package animal.shelter.animalsshelter.controllers.contexts.usercontext;

import animal.shelter.animalsshelter.controllers.TelegramBotStart;
import animal.shelter.animalsshelter.model.User;
import lombok.Data;

@Data
public class BotContext {
    private final TelegramBotStart bot;
    private final User user;
    private final String input;

    public static BotContext of(TelegramBotStart bot, User user, String text) {
        return new BotContext(bot, user, text);
    }

    private BotContext(TelegramBotStart bot, User user, String input) {
        this.bot = bot;
        this.user = user;
        this.input = input;
    }

    public TelegramBotStart getBot() {
        return bot;
    }

    public User getUser() {
        return user;
    }

    public String getInput() {
        return input;
    }
}
