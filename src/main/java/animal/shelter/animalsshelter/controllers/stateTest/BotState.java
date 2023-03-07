package animal.shelter.animalsshelter.controllers.stateTest;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public enum BotState {
    Start {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Тестовая регистрация");
        }

        @Override
        public BotState nextState() {
            return EnterName;
        }
    },

    EnterName {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите имя");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setName(context.getInput());
        }

        @Override
        public BotState nextState() {
            return EnterAge;
        }
    },

    EnterAge {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Введите возраст");
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
        }

        @Override
        public BotState nextState() {
            return Start;
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
        message.setChatId(Long.valueOf(context.getUser().getId()));
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
