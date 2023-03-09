package animal.shelter.animalsshelter.controllers.usercontext;

import animal.shelter.animalsshelter.controllers.TelegramBotStart;
import animal.shelter.animalsshelter.controllers.stateTest.TestUser;
import lombok.Data;

@Data
public class BotContext {
    private final TelegramBotStart bot;
    private final TestUser user;
    private final String input;

    public static BotContext of(TelegramBotStart bot, TestUser user, String text) {
        return new BotContext(bot, user, text);
    }

    private BotContext(TelegramBotStart bot, TestUser user, String input) {
        this.bot = bot;
        this.user = user;
        this.input = input;
    }

    public TelegramBotStart getBot() {
        return bot;
    }

    public TestUser getUser() {
        return user;
    }

    public String getInput() {
        return input;
    }
}
