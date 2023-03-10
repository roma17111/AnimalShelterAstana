package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.util.Emoji;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;

import static animal.shelter.animalsshelter.controllers.TelegramBotStart.*;

@Component
@Log4j
public class Keyboards {

    public static final String DOG = "DOG";
    public static final String CAT = "CAT";
    public static final String ACQUAINTANCE = "ACQUAINTANCE";
    public static final String DOCUMENTS = "DOCUMENTS";
    public static final String TRAVEL = "TRAVEL";
    public static final String HOME_PREPARATION_PUPPY = "PREPARATIONPUPPY";
    public static final String HOME_PREPARATION_DOG = "PREPARATIONDOG";
    public static final String HOME_PREPARATION_INVALID_DOG = "PREPARATIONDOGINVALID";
    public static final String TIPS_FROM_HANDLER = "TIPSHANDLER";
    public static final String CONTACT_HANDLER = "CHANDLER";
    public static final String REASONS = "REASONS";
    public static final String SAMPLE_REPORT = "SAMPLEREPORT";



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

    public SendMessage getBotStartUserMenu(long id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(id));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton shelterInfoButton = new InlineKeyboardButton();
        shelterInfoButton.setText("Информация о приюте");
        shelterInfoButton.setCallbackData(INFO_BUTTON);
        InlineKeyboardButton necessary = new InlineKeyboardButton();
        necessary.setText("Хотите животное? Важно знать!");
        necessary.setCallbackData(NECESSARY);
        InlineKeyboardButton report = new InlineKeyboardButton();
        report.setText("Отправить отчёт о животном");
        report.setCallbackData(SEND_REPORT);
        InlineKeyboardButton call = new InlineKeyboardButton();
        call.setText("Вопрос к волонтёру");
        call.setCallbackData(CALL_VOLUNTEER);
        row.add(shelterInfoButton);
        row1.add(necessary);
        row2.add(report);
        row3.add(call);
        rows.add(row);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        inlineKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText("Главное меню");
        return sendMessage;
    }

    public EditMessageText WhatNeedToKnow(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText("Обратите внимание!");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Знакомство с будущим питомцем");
        button1.setCallbackData(ACQUAINTANCE);
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Документы для оформления");
        button2.setCallbackData(DOCUMENTS);
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Путешествие с питомцем!");
        button3.setCallbackData(TRAVEL);
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Подготовить дом для щенка");
        button4.setCallbackData(HOME_PREPARATION_PUPPY);
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Подготовить дом для взрослой собаки");
        button5.setCallbackData(HOME_PREPARATION_DOG);
        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setCallbackData(HOME_PREPARATION_INVALID_DOG);
        button6.setText("Подготовиться к приему особенной собаки");
        InlineKeyboardButton button7 = new InlineKeyboardButton();
        button7.setCallbackData(TIPS_FROM_HANDLER);
        button7.setText("Советы кинолога");
        InlineKeyboardButton button8 = new InlineKeyboardButton();
        button8.setText("К какому кинологу обратиться?");
        button8.setCallbackData(CONTACT_HANDLER);
        InlineKeyboardButton button9 = new InlineKeyboardButton();
        button9.setCallbackData(REASONS);
        button9.setText("Причины отказа в получении питомца ");
        InlineKeyboardButton button10 = new InlineKeyboardButton();
        button10.setText("Образец отчета");
        button10.setCallbackData(SAMPLE_REPORT);
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   назад");
        backOne.setCallbackData(GO_BACK);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        List<InlineKeyboardButton> row6 = new ArrayList<>();
        List<InlineKeyboardButton> row7 = new ArrayList<>();
        List<InlineKeyboardButton> row8 = new ArrayList<>();
        row1.add(button1);
        row2.add(button2);
        row2.add(button3);
        row3.add(button4);
        row4.add(button5);
        row5.add(button6);
        row6.add(button7);
        row6.add(button8);
        row7.add(button9);
        row8.add(button10);
        row8.add(backOne);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
        rows.add(row6);
        rows.add(row7);
        rows.add(row8);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }
}
