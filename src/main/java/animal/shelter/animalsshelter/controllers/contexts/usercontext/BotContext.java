package animal.shelter.animalsshelter.controllers.contexts.usercontext;

import animal.shelter.animalsshelter.controllers.TelegramBotStart;
import animal.shelter.animalsshelter.model.User;
import lombok.Data;

/**
 * Класс BotContext представляет собой контекст бота для конкретного пользователя.
 * Содержит информацию о самом боте, пользователе и введенном пользователем тексте.
 */
@Data
public class BotContext {
    private final TelegramBotStart bot;
    private final User user;
    private final String input;

    /**
     * Создает новый объект BotContext с указанными параметрами.
     *
     * @param bot экземпляр класса TelegramBotStart, представляющий бота
     * @param user объект класса User, представляющий пользователя
     * @param text текст сообщения от пользователя
     * @return новый объект BotContext
     */
    public static BotContext of(TelegramBotStart bot, User user, String text) {
        return new BotContext(bot, user, text);
    }

    /**
     * Закрытый конструктор, используемый только внутри класса для создания новых объектов BotContext.
     *
     * @param bot - экземпляр класса TelegramBotStart, который используется для выполнения действий бота.
     * @param user - пользователь, для которого создан контекст.
     * @param input - введенный пользователем текст.
     */
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
