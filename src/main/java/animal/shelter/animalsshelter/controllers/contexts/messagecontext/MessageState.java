package animal.shelter.animalsshelter.controllers.contexts.messagecontext;

import animal.shelter.animalsshelter.util.Emoji;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static animal.shelter.animalsshelter.controllers.Keyboards.BACK_QUESTION;

/**
 * Перечисление MessageState представляет возможные состояния диалога пользователя с ботом.
 * Каждое состояние имеет методы для входа в состояние, обработки входных данных и перехода к следующему состоянию.
 * Состояния могут быть с вводом данных или без ввода данных.
 */
public enum MessageState {

    EnterQuestion (false){

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
            return End;
        }
    },

    End {
        @Override
        public void enter(MessageContext context) {

        }

        @Override
        public MessageState nextState() {
            return End;
        }
    };

    private static MessageState[] states;
    private final boolean inputNeeded;

    MessageState() {
        this.inputNeeded = true;
    }

    MessageState(boolean inputNeeded) {
        this.inputNeeded = inputNeeded;
    }

    public static MessageState getInitialState() {
        return byId(0);
    }

    public static MessageState byId(int id) {
        if (states == null) {
            states = MessageState.values();
        }

        return states[id];
    }

    protected void sendMessage(MessageContext context, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(context.getMsg().getChatID());
        message.setText(text);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        row.add(button);
        rows.add(row);
        button.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   назад");
        button.setCallbackData(BACK_QUESTION);
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);
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
