package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.config.Config;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class BotMenu1 extends TelegramBotStart{

    private static final String TELL_ABOUT_SHELTER = "TELL_ME";
    private static final String WORK_TIME = "TIME_BUTTON";
    private static final String ADDRESS = "ADDRESS_BUTTON";
    private static final String SECURITY = "SECURITY_BUTTON";
    private static final String REGISTRATION = "REGISTRATION_BUTTON";


    public BotMenu1(Config config) {
        super(config);
    }

    public void startMenu1(long chatId, long messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId((int) messageId);
        editMessageText.setText("Привет - Я Asha. \n Чем могу помочь?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();


    }
}
