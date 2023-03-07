package animal.shelter.animalsshelter.controllers.stateTest;

import animal.shelter.animalsshelter.controllers.TelegramBotStart;
import animal.shelter.animalsshelter.model.TestEntity;

public class BotContext {

    private final TelegramBotStart bot;
    private final TestEntity entity;
    private final String input;

    public static BotContext of(TelegramBotStart bot, TestEntity entity, String text) {
        return new BotContext(bot, entity, text);
    }

    private BotContext(TelegramBotStart bot, TestEntity entity, String input) {
        this.bot = bot;
        this.entity = entity;
        this.input = input;
    }

    public TelegramBotStart getBot() {
        return bot;
    }

    public TestEntity getUser() {
        return entity;
    }

    public String getInput() {
        return input;
    }
}
