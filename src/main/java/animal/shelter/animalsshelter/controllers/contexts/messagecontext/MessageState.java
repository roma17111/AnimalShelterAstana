package animal.shelter.animalsshelter.controllers.contexts.messagecontext;

import animal.shelter.animalsshelter.controllers.contexts.usercontext.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public enum MessageState {

    EnterQuestion {

        @Override
        public void enter(MessageContext context) {
            sendMessage(context, "Какой у вас вопрос?");
        }

        @Override
        public void handleInput(MessageContext context) {
            context.getMsg().setMsgText(context.getInput());
        }

        @Override
        public MessageState nextState() {
            return ApprovedMsg;
        }
    },
    ApprovedMsg(false){
        @Override
        public void enter(MessageContext context) {
            sendMessage(context, "Ваш вопрос отправлен");
            sendMessage(context, "С вами свяжутся в ближайшее время");
        }

        @Override
        public MessageState nextState() {
            return null;
        }
    };

    private static BotState[] states;
    private final boolean inputNeeded;

    MessageState() {
        this.inputNeeded = true;
    }

    MessageState(boolean inputNeeded) {
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

    protected void sendMessage(MessageContext context, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(context.getMsg().getChatID());
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

    public void handleInput(MessageContext context) {
        // do nothing by default
    }

    public abstract void enter(MessageContext context);

    public abstract MessageState nextState();
}
