package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.config.Config;
import animal.shelter.animalsshelter.controllers.usercontext.BotContext;
import animal.shelter.animalsshelter.controllers.usercontext.BotState;
import animal.shelter.animalsshelter.controllers.stateTest.TestUser;
import animal.shelter.animalsshelter.controllers.stateTest.TestUserService;
import animal.shelter.animalsshelter.repository.UserRepository;
import animal.shelter.animalsshelter.service.ImageParser;
import animal.shelter.animalsshelter.service.impl.ImageParserImpl;
import animal.shelter.animalsshelter.util.Emoji;
import animal.shelter.animalsshelter.util.StartMenu;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Log4j
@Component
@RestController
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
    private final ImageParser imageParser = new ImageParserImpl(this);

    @Autowired
    private UserRepository userRepository;

    private final TestUserService userService;


    public TelegramBotStart(Config config, TestUserService userService) {
        this.config = config;
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotKey();
    }

    /**
     * Метод обновления, который вызывается, когда бот получает новое сообщение.
     * Если сообщение содержит текст, то выполняется один из кейсов.
     * В кейсах происходит отправка сообщений, вызов различных методов бота и логирование действий.
     * Если сообщение содержит callback-запрос, то выполняется соответствующее действие в зависимости от полученных данных.
     * Метод использует аннотацию @SneakyThrows для удобной обработки исключений.
     */
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
                case "/test":
                    break;
                default:
                 /*   String msg = "Вопрос пользователя: \n"
                            + update.getMessage().getChatId() + "\n"
                            + update.getMessage().getChat().getFirstName() + "\n"
                            + update.getMessage().getChat().getLastName() + "\n"
                            + message.getText();
                    sendBotMessage(453006669, msg);*/
                    //  sendBotMessage(update.getMessage().getChatId(), msg);

                    testReg(update);
                    System.out.println(message.getText());
                    System.out.println(message.getMessageId());
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
                getBackMenu(chatId, messageId);
            } else if (dataCallback.equals(BACK_ONE_POINT)) {
                startMenu1(chatId, messageId);
            } else if (dataCallback.equals(TELL_ABOUT_SHELTER)) {
                getInfoAboutMe(chatId, messageId);
            } else if (dataCallback.equals(CALL_VOLUNTEER)) {
                callVolunteer(chatId, messageId);
                EditMessageText text = new EditMessageText();
                Message message1 = new Message();
            } else if (dataCallback.equals(WORK_TIME)) {
                getWorkTime(chatId, messageId);
            } else if (dataCallback.equals(ADDRESS)) {
                getContactUs(chatId, messageId);
            } else if (dataCallback.equals(SECURITY)) {
                getSafeInformation(chatId, messageId);
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

    private void testReg(Update update) {
        final String text = update.getMessage().getText();
        final long chatId = update.getMessage().getChatId();
        TestUser user = userService.findByChatId(chatId);
        BotContext context;
        BotState state;
        if (user == null) {
            state = BotState.getInitialState();
            user = new TestUser(update.getMessage().getChatId(), state.ordinal());
            userService.addUser(user);
            context = BotContext.of(this, user, text);
            state.enter(context);
            log.info("New user registered: " + chatId);
            user.setName(update.getMessage().getChat().getFirstName());
        } else {
            context = BotContext.of(this, user, text);
            state = BotState.byId(user.getStateId());

            log.info("Update received for user in state: " + state);
        }
        state.handleInput(context);

        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());
        user.setStateId(state.ordinal());
        userService.updateUser(user);
    }

    /**
     * Отправляет сообщение пользователю в Telegram боте.
     *
     * @param id   идентификатор чата
     * @param name текст сообщения для отправки
     */
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

    /**
     * Метод для отправки пользователю главного меню бота при старте бота.
     *
     * @param id ID чата с пользователем
     */
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

    /**
     * Метод для отправки фото из url адресса проекта.
     *
     * @param id - id чата, куда будет отправлено фото.
     */
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

    /**
     * Метод для вывода меню при старте бота
     *
     * @param chatId    - идентификатор чата
     * @param messageId - идентификатор сообщения
     */
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
        tellMe.setText("Информация о нас");
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
        back.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   назад");
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

    /**
     * Метод отображает главное меню бота, куда можно вернуться из других разделов.
     *
     * @param id        идентификатор чата в телеграм
     * @param messageId идентификатор сообщения в чате
     */
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

    /**
     * Метод для отправки сообщения с информацией о приюте в чат.
     *
     * @param chatID    ID чата.
     * @param messageId ID сообщения.
     */
    private void getInfoAboutMe(long chatID, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatID);
        messageText.setMessageId((int) messageId);
        messageText.setText(startMenu.getInfoAboutShelter());
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   назад");
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

    /**
     * Метод для вызова волонтера.
     *
     * @param chatID    - идентификатор чата в Телеграме
     * @param messageId - идентификатор сообщения в чате
     */
    private void callVolunteer(long chatID,
                               long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatID);
        messageText.setMessageId((int) messageId);
        messageText.setText("Какой у вас вопрос?");
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   назад");
        backOne.setCallbackData(GO_BACK);
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

    /**
     * Метод для отправки пользователю сообщения о рабочем времени.
     *
     * @param chatID    ID чата, в котором отправляется сообщение.
     * @param messageId ID сообщения, которое нужно отредактировать.
     */
    private void getWorkTime(long chatID,
                             long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatID);
        messageText.setMessageId((int) messageId);
        messageText.setText(EmojiParser.parseToUnicode(startMenu.workTime()));
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   назад");
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

    /**
     * Метод, отправляющий сообщение с информацией о контактах бота и кнопкой "назад".
     *
     * @param chatID    - идентификатор чата
     * @param messageId - идентификатор сообщения
     */
    private void getContactUs(long chatID,
                              long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatID);
        messageText.setMessageId((int) messageId);
        messageText.setText(EmojiParser.parseToUnicode(startMenu.contactUs()));
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   назад");
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

    /**
     * Отправляет пользователю сообщение с инструкцией по обеспечению безопасности
     * в EditMessageText формате с соответствующей клавиатурой InlineKeyboardMarkup.
     *
     * @param chatID    id чата
     * @param messageId id сообщения
     */
    private void getSafeInformation(long chatID,
                                    long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatID);
        messageText.setMessageId((int) messageId);
        messageText.setText(EmojiParser.parseToUnicode(startMenu.toBeSafeRegulations()));
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton backOne = new InlineKeyboardButton();
        backOne.setText(EmojiParser.parseToUnicode(Emoji.BACK_POINT_HAND_LEFT) + "   назад");
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

    /**
     * Сделать чуть позже
     */
    private void getPhotoFromMessage(Message message) {
        try {
            if (message.hasPhoto()) {
                Long chatId = message.getChatId();
                PhotoSize photo = imageParser.getLargestPhoto(message.getPhoto());
                byte[] byteCode = imageParser.imageToByteCode(photo);
                // sendPhoto(chatId, byteCode);
                // Отправка данных будет производиться на сервер
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод отправляет фотографию в формате байтового кода пользователю в чате
     *
     * @param chatId   идентификатор чата
     * @param byteCode байтовый код фотографии
     */
    private void sendPhotoFromByteCode(Long chatId, byte[] byteCode) {
        try {
            execute(imageParser.byteCodeToImage(chatId, byteCode));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
