package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.config.Config;
import animal.shelter.animalsshelter.services.Emoji;
import animal.shelter.animalsshelter.services.StartMenu;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Log4j
@Component
public class TelegramBotStart extends TelegramLongPollingBot {
    private static final String TELL_ABOUT_SHELTER = "TELL_ME";
    private static final String WORK_TIME = "TIME_BUTTON";
    private static final String ADDRESS = "ADDRESS_BUTTON";
    private static final String SECURITY = "SECURITY_BUTTON";
    private static final String REGISTRATION = "REGISTRATION_BUTTON";

    private static final String INFO_BUTTON = "INFO_BUTTON";
    private static final String NECESSARY = "NECESSARY_TO_GET_ANIMAL";
    private static final String SEND_REPORT = "SEND_REPORT";
    private static final String CALL_VOLUNTEER = "CALL_VOLUNTEER";
    private static final String GO_BACK = "GO_BACK";
    private static final String BACK_ONE_POINT = "BACK_ONE";
    private static final String URL_START_PHOTO = "src/main/resources/templates/msg6162958373-22385.jpg";

    private final Config config;
    private final StartMenu startMenu = new StartMenu();

    public TelegramBotStart(Config config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotKey();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    String hello = EmojiParser.parseToUnicode(startMenu.sayHello());
                    log.info(hello);
                    sendBotMessage(update.getMessage().getChatId(), "Привет - Это Asha)");
                    log.info(update.getMessage().getChatId() + " Привет - Это Asha)");
                    sendPhoto(update.getMessage().getChatId());
                    Thread.sleep(500);
                    sendBotMessage(update.getMessage().getChatId(), hello);
                    Thread.sleep(1200);
                    getBotStartUserMenu(update.getMessage().getChatId());
                    break;
                case "/menu":
                    getBotStartUserMenu(update.getMessage().getChatId());
                    break;
                default:
                    sendBotMessage(update.getMessage().getChatId(), "Вы ввели - " + message.getText());
                    System.out.println(message.getText());
                    log.info(update.getMessage().getChatId() + " " + message.getText());
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String dataCallback = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            if (dataCallback.equals(INFO_BUTTON)) {
                startMenu1(chatId, messageId);
            } else if (dataCallback.equals(GO_BACK)) {
                getBackMenu(chatId,messageId);
            } else if (dataCallback.equals(BACK_ONE_POINT)) {
                startMenu1(chatId, messageId);
            } else if (dataCallback.equals(TELL_ABOUT_SHELTER)) {
                getInfoAboutMe(chatId,messageId);
            } else {
                EditMessageText messageText = new EditMessageText();
                messageText.setChatId(chatId);
                messageText.setMessageId((int) messageId);
                messageText.setText("Пока в разработке)))");
                try {
                    execute(messageText);
                } catch (TelegramApiException e) {
                    log.error(e.getMessage());
                }
                Thread.sleep(400);
                getBackMenu(chatId, messageId);
            }
        }
    }

    private void sendBotMessage(long id, String name) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(id));
        sendMessage.setText(name);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void getBotStartUserMenu(long id) {
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
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendPhoto(long id) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(id));
        sendPhoto.setPhoto(new InputFile(new File(URL_START_PHOTO)));
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void startMenu1(long chatId, long messageId) {
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
        List<InlineKeyboardButton> row6 = new ArrayList<>();
        InlineKeyboardButton tellMe = new InlineKeyboardButton();
        tellMe.setText("Расскажи о себе");
        tellMe.setCallbackData(TELL_ABOUT_SHELTER);
        InlineKeyboardButton cLockWork = new InlineKeyboardButton();
        cLockWork.setText("Часы работы");
        cLockWork.setCallbackData(WORK_TIME);
        InlineKeyboardButton addressShelter = new InlineKeyboardButton();
        addressShelter.setText("Адрес и схема проезда");
        addressShelter.setCallbackData(ADDRESS);
        InlineKeyboardButton recommendations = new InlineKeyboardButton();
        recommendations.setText("Техника безопасности");
        recommendations.setCallbackData(SECURITY);
        InlineKeyboardButton registerMe = new InlineKeyboardButton();
        registerMe.setText("Оставить контактные данные");
        registerMe.setCallbackData(REGISTRATION);
        InlineKeyboardButton volunteerCall = new InlineKeyboardButton();
        volunteerCall.setText("Вопрос к волонтёру");
        volunteerCall.setCallbackData(CALL_VOLUNTEER);
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText( EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT)+"   назад");
        back.setCallbackData(GO_BACK);
        row1.add(tellMe);
        row2.add(cLockWork);
        row2.add(addressShelter);
        row3.add(recommendations);
        row4.add(registerMe);
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
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void getBackMenu(long id, long messageId) {
        EditMessageText sendMessage = new EditMessageText();
        sendMessage.setChatId(id);
        sendMessage.setMessageId((int) messageId);
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
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void getInfoAboutMe(long chatID, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatID);
        messageText.setMessageId((int) messageId);
        messageText.setText(startMenu.getInfoAboutShelter());
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT)+"   назад");
        backOne.setCallbackData(BACK_ONE_POINT);
        row.add(backOne);
        rows.add(row);
        keyboardMarkup.setKeyboard(rows);
        messageText.setReplyMarkup(keyboardMarkup);
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

    }
}
