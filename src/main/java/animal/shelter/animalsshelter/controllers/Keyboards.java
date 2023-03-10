package animal.shelter.animalsshelter.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j
public class Keyboards {

    public static final String DOG = "DOG";
    public static final String CAT = "CAT";


    public Keyboards() {
    }

    public SendMessage getTypeOfShelter(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выберите приют");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton catButton = new InlineKeyboardButton();
        catButton.setCallbackData(CAT);
        catButton.setText("Приют для кошек");
        InlineKeyboardButton dogButton = new InlineKeyboardButton();
        dogButton.setCallbackData(DOG);
        dogButton.setText("Приют для собак");
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row1.add(catButton);
        row2.add(dogButton);
        rows.add(row1);
        rows.add(row2);
        markup.setKeyboard(rows);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }
}
