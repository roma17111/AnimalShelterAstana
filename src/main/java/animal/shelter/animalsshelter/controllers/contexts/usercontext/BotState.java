package animal.shelter.animalsshelter.controllers.contexts.usercontext;

import animal.shelter.animalsshelter.util.Utils;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс представляет собой состояния, в которых может находиться Telegram-бот.
 * Каждое состояние описывает, как бот должен взаимодействовать с пользователем и какое следующее состояние должно быть.
 */
@Log4j
public enum BotState {

    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Добро пожаловать! ");
        }

        @Override
        public BotState nextState() {
            return EnterPhone;
        }
    },

    EnterPhone {

        private BotState next;
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите номер телефона");
        }

        @Override
        public void handleInput(BotContext context) {
            String regex = "[0-9*#+() -]*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(context.getInput());
            if (context.getInput().equals("/registration")||!matcher.matches()) {
                next = EnterPhone;
            } else {
                context.getUser().setPhoneNumber(context.getInput());
                next = EnterEmail;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    EnterEmail {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите e-mail:");
        }

        @Override
        public void handleInput(BotContext context) {
            String email = context.getInput();

            if (Utils.isValidEmailAddress(email)) {
                context.getUser().setEmail(context.getInput());
                next = Approved;
            } else {
                sendMessage(context, "Некорректный e-mail address!");
                next = EnterEmail;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },

    Approved {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Спасибо за регистрацию");
            sendMessage(context, context.getUser().toString());
        }

        @Override
        public BotState nextState() {
            return End;
        }
    },
    End{
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Вы уже зарегистрированы!!!");
            sendMessage(context, "Вот ваши данные");
            sendMessage(context, context.getUser().toString());
        }

        @Override
        public BotState nextState() {
            return EndBack;
        }
    },
    EndBack {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Вы уже зарегистрированы!!!");
            sendMessage(context, "Вот ваши данные");
            sendMessage(context, context.getUser().toString());
        }

        @Override
        public BotState nextState() {
            return End;
        }
    };

    private static BotState[] states;
    private final boolean inputNeeded;

    BotState() {
        this.inputNeeded = true;
    }

    BotState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }

        return states[id];
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(context.getUser().getChatId());
        message.setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean isInputNeeded() {
        return inputNeeded;
    }

    public void handleInput(BotContext context) {
        // do nothing by default
    }

    public abstract void enter(BotContext context);

    public abstract BotState nextState();

}
