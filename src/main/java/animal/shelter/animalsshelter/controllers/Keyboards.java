package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.util.AdoptingDocumentsAndRules;
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
    public static final String BACK_TWO = "BACKTWO";

    public static final String BACK_REPORT = "BACKREPORT";
    public static final String BACK_ADD_DOG = "BACKDOG";

    public static final String PUPPY_TYPE = "PUPPY";
    public static final String ADULT_TYPE = "ADULT";
    public static final String DISABLED_TYPE = "DISABLED";

    public static final String BACK_QUESTION = "BACKQUESTION";

    public static final String NECESSARY_CAT = "NCAT";

    public static final String GO_BACK_CAT = "GO_BACK_CAT";
    public static final String INFO_BUTTON_CAT = "INFO_BUTTON_CAT";
    public static final String DOC_CAT = "DOCCAT";
    public static final String BACK_CAT_ONE = "BACK_CAT_ONE";
    public static final String BACK_CAT_TWO = "BACK_CAT_TWO";
    public static final String HOME_PREPARATION_KITTEN = "HOME_PREPARATION_KITTEN";
    public static final String ACQUAINTANCE_CAT = "ACQUAINTANCE_CAT";
    public static final String TRAVEL_CAT = "TRAVEL_CAT";
    public static final String HOME_PREPARATION_CAT = "HOME_PREPARATION_CAT ";
    public static final String HOME_PREPARATION_INVALID_CAT = "HOME_PREPARATION_INVALID_CAT";
    public static final String REASONS_CAT = "REASONS_CAT";
    public static final String SAMPLE_REPORT_CAT = "SAMPLE_REPORT_CAT";
    public static final String TELL_ABOUT_SHELTER_CAT = "TELL_ABOUT_SHELTER_CAT";
    public static final String WORK_TIME_CAT = "WORK_TIME_CAT";
    public static final String ADDRESS_CAT = "ADDRESS_CAT";
    public static final String SECURITY_CAT = "SECURITY_CAT";
    public static final String GO_START = "GO_START";
    public static final String GALLERY_DOG = "GALLERY_DOG ";


    public Keyboards() {
    }

    public EditMessageText messageTextCatOne(long chatId, long messageId, String text) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(text);
        InlineKeyboardMarkup m = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(GO_BACK_CAT);
        row.add(back);
        rows.add(row);
        m.setKeyboard(rows);
        messageText.setReplyMarkup(m);
        return messageText;
    }

    public EditMessageText messageTextCatTwo(long chatId, long messageId, String text) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(text);
        InlineKeyboardMarkup m = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_CAT_TWO);
        row.add(back);
        rows.add(row);
        m.setKeyboard(rows);
        messageText.setReplyMarkup(m);
        return messageText;
    }

    public SendMessage getTypeOfShelter(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("???????????????? ??????????");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton catButton = new InlineKeyboardButton();
        catButton.setCallbackData(CAT);
        catButton.setText("?????????? ?????? ??????????");
        InlineKeyboardButton dogButton = new InlineKeyboardButton();
        dogButton.setCallbackData(DOG);
        dogButton.setText("?????????? ?????? ??????????");
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

    public EditMessageText getTypeOfShelterEdit(long chatId, long messageId) {
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setChatId(chatId);
        sendMessage.setMessageId((int) messageId);
        sendMessage.setText("???????????????? ??????????");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton catButton = new InlineKeyboardButton();
        catButton.setCallbackData(CAT);
        catButton.setText("?????????? ?????? ??????????");
        InlineKeyboardButton dogButton = new InlineKeyboardButton();
        dogButton.setCallbackData(DOG);
        dogButton.setText("?????????? ?????? ??????????");
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

    /**
     * ?????????? ?????? ???????????????? ???????????????????????? ???????????????? ???????? ???????? ?????? ???????????? ????????.
     *
     * @param id ID ???????? ?? ??????????????????????????
     */

    public SendMessage getBotStartUserMenu(long id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(id));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton shelterInfoButton = new InlineKeyboardButton();
        shelterInfoButton.setText("???????????????????? ?? ????????????");
        shelterInfoButton.setCallbackData(INFO_BUTTON);
        InlineKeyboardButton necessary = new InlineKeyboardButton();
        necessary.setText("???????????? ????????????? ?????????? ??????????!");
        necessary.setCallbackData(NECESSARY);
        InlineKeyboardButton report = new InlineKeyboardButton();
        report.setText("?????????????????? ?????????? ?? ????????????????");
        report.setCallbackData(SEND_REPORT);
        InlineKeyboardButton call = new InlineKeyboardButton();
        call.setText("???????????? ?? ??????????????????");
        call.setCallbackData(CALL_VOLUNTEER);
        row.add(shelterInfoButton);
        row1.add(necessary);
        row2.add(report);
        row3.add(call);
        rows.add(row);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        inlineKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText("?????????????? ????????");
        return sendMessage;
    }

    public SendMessage getBotStartUserMenuCat(long id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(id));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton shelterInfoButton = new InlineKeyboardButton();
        shelterInfoButton.setText("???????????????????? ?? ????????????");
        shelterInfoButton.setCallbackData(INFO_BUTTON_CAT);
        InlineKeyboardButton necessary = new InlineKeyboardButton();
        necessary.setText("???????????? ??????????? ?????????? ??????????!");
        necessary.setCallbackData(NECESSARY_CAT);
        InlineKeyboardButton report = new InlineKeyboardButton();
        report.setText("?????????????????? ?????????? ?? ????????????????");
        report.setCallbackData(SEND_REPORT);
        InlineKeyboardButton call = new InlineKeyboardButton();
        call.setText("???????????? ?? ??????????????????");
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
        sendMessage.setText("?????????????? ????????");
        return sendMessage;
    }

    public EditMessageText getBotStartUserMenuCatBack(long id, long messageID) {
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setChatId(String.valueOf(id));
        sendMessage.setMessageId((int) messageID);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton shelterInfoButton = new InlineKeyboardButton();
        shelterInfoButton.setText("???????????????????? ?? ????????????");
        shelterInfoButton.setCallbackData(INFO_BUTTON_CAT);
        InlineKeyboardButton necessary = new InlineKeyboardButton();
        necessary.setText("???????????? ??????????? ?????????? ??????????!");
        necessary.setCallbackData(NECESSARY_CAT);
        InlineKeyboardButton report = new InlineKeyboardButton();
        report.setText("?????????????????? ?????????? ?? ????????????????");
        report.setCallbackData(SEND_REPORT);
        InlineKeyboardButton call = new InlineKeyboardButton();
        call.setText("???????????? ?? ??????????????????");
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
        sendMessage.setText("?????????????? ????????");
        return sendMessage;
    }

    public EditMessageText WhatNeedToKnow(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText("???????????????? ????????????????!");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("???????????????????? ?? ?????????????? ????????????????");
        button1.setCallbackData(ACQUAINTANCE);
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("?????????????????? ?????? ????????????????????");
        button2.setCallbackData(DOCUMENTS);
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("?????????????????????? ?? ????????????????!");
        button3.setCallbackData(TRAVEL);
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("?????????????????????? ?????? ?????? ??????????");
        button4.setCallbackData(HOME_PREPARATION_PUPPY);
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("?????????????????????? ?????? ?????? ???????????????? ????????????");
        button5.setCallbackData(HOME_PREPARATION_DOG);
        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setCallbackData(HOME_PREPARATION_INVALID_DOG);
        button6.setText("?????????????????????????? ?? ???????????? ?????????????????? ????????????");
        InlineKeyboardButton button7 = new InlineKeyboardButton();
        button7.setCallbackData(TIPS_FROM_HANDLER);
        button7.setText("???????????? ????????????????");
        InlineKeyboardButton button8 = new InlineKeyboardButton();
        button8.setText("?? ???????????? ???????????????? ?????????????????????");
        button8.setCallbackData(CONTACT_HANDLER);
        InlineKeyboardButton button9 = new InlineKeyboardButton();
        button9.setCallbackData(REASONS);
        button9.setText("?????????????? ???????????? ?? ?????????????????? ?????????????? ");
        InlineKeyboardButton galleryDog = new InlineKeyboardButton();
        galleryDog.setText("???????????????? ??????????");
        galleryDog.setCallbackData(GALLERY_DOG);
        InlineKeyboardButton button10 = new InlineKeyboardButton();
        button10.setText("?????????????? ????????????");
        button10.setCallbackData(SAMPLE_REPORT);
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        backOne.setCallbackData(GO_BACK);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        List<InlineKeyboardButton> row6 = new ArrayList<>();
        List<InlineKeyboardButton> row7 = new ArrayList<>();
        List<InlineKeyboardButton> galleryRow = new ArrayList<>();
        galleryRow.add(galleryDog);
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
        rows.add(galleryRow);
        rows.add(row8);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText WhatNeedToKnowAboutCat(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText("???????????????? ????????????????!");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("???????????????????? ?? ?????????????? ????????????????");
        button1.setCallbackData(ACQUAINTANCE_CAT);
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("?????????????????? ?????? ????????????????????");
        button2.setCallbackData(DOC_CAT);
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("?????????????????????? ?? ????????????????!");
        button3.setCallbackData(TRAVEL_CAT);
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("?????????????????????? ?????? ?????? ??????????????");
        button4.setCallbackData(HOME_PREPARATION_KITTEN);
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("?????????????????????? ?????? ?????? ???????????????? ??????????");
        button5.setCallbackData(HOME_PREPARATION_CAT);
        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setCallbackData(HOME_PREPARATION_INVALID_CAT);
        button6.setText("?????????????????????????? ?? ???????????? ?????????????????? ??????????");
        InlineKeyboardButton button9 = new InlineKeyboardButton();
        button9.setCallbackData(REASONS_CAT);
        button9.setText("?????????????? ???????????? ?? ?????????????????? ?????????????? ");
        InlineKeyboardButton button10 = new InlineKeyboardButton();
        button10.setText("?????????????? ????????????");
        button10.setCallbackData(SAMPLE_REPORT_CAT);
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        backOne.setCallbackData(BACK_CAT_ONE);
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

    public EditMessageText getWindowOne(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.MEETING_DOG_RULES);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getWindowTwo(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.DOCUMENTS_FOR_ADOPTION);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getWindowThree(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.TRAVELLING_WITH_PET);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getWindowFour(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.PLACE_FOR_PUPPY);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getWindowFive(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.PLACE_FOR_DOG);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getWindowSix(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.PLACE_FOR_DISABLED_DOG);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getWindowSeven(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.CYNOLOGIST_ADVISES);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getWindowEight(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.LIST_OF_CYNOLOGISTS);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getWindowNine(long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(AdoptingDocumentsAndRules.ADOPTING_DENY_REASONS);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_TWO);
        buttons.add(back);
        rows.add(buttons);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public SendMessage getBackButtonForReport(long chatId,
                                              String text) {
        SendMessage messageText = new SendMessage();
        messageText.setChatId(chatId);
        messageText.setText(text);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        row.add(button);
        rows.add(row);
        button.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        button.setCallbackData(BACK_REPORT);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public SendMessage getBackButtonForDog(long chatId,
                                           String text) {
        SendMessage messageText = new SendMessage();
        messageText.setChatId(chatId);
        messageText.setText(text);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        row.add(button);
        rows.add(row);
        button.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        button.setCallbackData(BACK_ADD_DOG);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public SendMessage getDogTypeButton(long chatId, String text) {
        SendMessage messageText = new SendMessage();
        messageText.setChatId(chatId);
        messageText.setText(text);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton puppyButton = new InlineKeyboardButton();
        InlineKeyboardButton adultButton = new InlineKeyboardButton();
        InlineKeyboardButton disabledButton = new InlineKeyboardButton();
        InlineKeyboardButton backButton = new InlineKeyboardButton();

        row.add(puppyButton);
        row.add(adultButton);
        row.add(disabledButton);
        row.add(backButton);
        rows.add(row);

        puppyButton.setText("??????????");
        puppyButton.setCallbackData(PUPPY_TYPE);

        adultButton.setText("????????????????");
        adultButton.setCallbackData(ADULT_TYPE);

        disabledButton.setText("?? ?????????????????????????? ??????????????????????????");
        disabledButton.setCallbackData(DISABLED_TYPE);

        backButton.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        backButton.setCallbackData(BACK_ADD_DOG);

        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getBackButtonForQuestion(long chatId,
                                                    long messageId,
                                                    String text) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageId);
        messageText.setText(text);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        row.add(button);
        rows.add(row);
        button.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        button.setCallbackData(BACK_QUESTION);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public SendMessage getMenuAfterAnswer(long chatId) {
        SendMessage messageText = new SendMessage();
        messageText.setChatId(chatId);
        messageText.setText("???????????? ?????? ???????????? ?????????????");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("??????");
        button.setCallbackData(GO_START);
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("????");
        button1.setCallbackData(CALL_VOLUNTEER);
        row.add(button1);
        row.add(button);
        rows.add(row);
        markup.setKeyboard(rows);
        messageText.setReplyMarkup(markup);
        return messageText;
    }

    public EditMessageText getAboutShelterCat(long chatId, long messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId((int) messageId);
        editMessageText.setText("???????????? - ?? Asha. \n ?????? ???????? ?????????????");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        List<InlineKeyboardButton> row6 = new ArrayList<>();
        InlineKeyboardButton tellMe = new InlineKeyboardButton();
        tellMe.setText("???????????????????? ?? ??????");
        tellMe.setCallbackData(TELL_ABOUT_SHELTER_CAT);
        InlineKeyboardButton cLockWork = new InlineKeyboardButton();
        cLockWork.setText("???????? ????????????");
        cLockWork.setCallbackData(WORK_TIME_CAT);
        InlineKeyboardButton addressShelter = new InlineKeyboardButton();
        addressShelter.setText("?????????? ?? ?????????? ??????????????");
        addressShelter.setCallbackData(ADDRESS_CAT);
        InlineKeyboardButton recommendations = new InlineKeyboardButton();
        recommendations.setText("?????????????? ????????????????????????");
        recommendations.setCallbackData(SECURITY_CAT);
        InlineKeyboardButton volunteerCall = new InlineKeyboardButton();
        volunteerCall.setText("???????????? ?? ??????????????????");
        volunteerCall.setCallbackData(CALL_VOLUNTEER);
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   ??????????");
        back.setCallbackData(BACK_CAT_ONE);
        row1.add(tellMe);
        row2.add(cLockWork);
        row2.add(addressShelter);
        row3.add(recommendations);
        row5.add(volunteerCall);
        row6.add(back);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
        rows.add(row6);
        inlineKeyboardMarkup.setKeyboard(rows);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        return editMessageText;
    }
}
