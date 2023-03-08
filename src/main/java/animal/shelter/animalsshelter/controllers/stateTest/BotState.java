package animal.shelter.animalsshelter.controllers.stateTest;

import animal.shelter.animalsshelter.util.Utils;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
public enum BotState {

    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Привет. Тестовая регистрация");
        }

        @Override
        public BotState nextState() {
            return EnterPhone;
        }
    },

    EnterPhone {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите номер телефона");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setNumberPhone(context.getInput());
        }

        @Override
        public BotState nextState() {
            return EnterEmail;
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

    Approved(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Спасибо за регистрацию");
            sendMessage(context, context.getUser().toString());
        }

        @Override
        public BotState nextState() {
            return null;
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
