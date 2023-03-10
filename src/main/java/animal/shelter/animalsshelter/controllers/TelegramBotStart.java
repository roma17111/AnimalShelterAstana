package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.config.Config;
import animal.shelter.animalsshelter.controllers.contexts.messagecontext.MessageContext;
import animal.shelter.animalsshelter.controllers.contexts.messagecontext.MessageState;
import animal.shelter.animalsshelter.controllers.contexts.usercontext.BotContext;
import animal.shelter.animalsshelter.controllers.contexts.usercontext.BotState;
import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.CallVolunteerMsgService;
import animal.shelter.animalsshelter.service.ImageParser;
import animal.shelter.animalsshelter.service.ReportService;
import animal.shelter.animalsshelter.service.UserService;
import animal.shelter.animalsshelter.service.impl.ImageParserImpl;
import animal.shelter.animalsshelter.util.Emoji;
import animal.shelter.animalsshelter.util.StartMenu;
import com.vdurmont.emoji.EmojiParser;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static animal.shelter.animalsshelter.controllers.Keyboards.CAT;
import static animal.shelter.animalsshelter.controllers.Keyboards.DOG;
import static animal.shelter.animalsshelter.util.Emoji.CAT_FACE;
import static animal.shelter.animalsshelter.util.Emoji.DOG_FACE;


@Log4j
@Component
public class TelegramBotStart extends TelegramLongPollingBot {
    public static final String TELL_ABOUT_SHELTER = "TELL_ME";
    public static final String WORK_TIME = "TIME_BUTTON";
    public static final String ADDRESS = "ADDRESS_BUTTON";
    public static final String SECURITY = "SECURITY_BUTTON";
    public static final String REGISTRATION = "REGISTRATION_BUTTON";
    public static final String INFO_BUTTON = "INFO_BUTTON";
    public static final String NECESSARY = "NECESSARY_TO_GET_ANIMAL";
    public static final String SEND_REPORT = "SEND_REPORT";
    public static final String CALL_VOLUNTEER = "CALL_VOLUNTEER";
    public static final String GO_BACK = "GO_BACK";
    public static final String BACK_ONE_POINT = "BACK_ONE";
    public static final String URL_START_PHOTO = "src/main/resources/templates/msg6162958373-22385.jpg";

    private final Config config;
    private final StartMenu startMenu = new StartMenu();
    private final ImageParser imageParser = new ImageParserImpl(this);
    private final UserService userService;

    private final ReportService reportService;
    private final CallVolunteerMsgService callVolunteerMsg;

    private final Keyboards keyboards = new Keyboards();
    public TelegramBotStart(Config config,
                            UserService userService,
                            ReportService reportService,
                            CallVolunteerMsgService callVolunteerMsg) {
        this.config = config;
        this.userService = userService;
        this.reportService = reportService;
        this.callVolunteerMsg = callVolunteerMsg;
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
                  startBot(update);
                    break;
                case "/menu":
                    execute(keyboards.getBotStartUserMenu(update.getMessage().getChatId()));
                    break;
                case "/registration":
                    testReg(update);
                    break;
                case "/allreports":
                    getAllReportsFromBot(update);
                default:
                   getDefaultSwitchRealisation(update,message);
                    break;
            }
        }
        getIfCallbackQuery(update);
        if (update.hasMessage()&& message.hasPhoto()) {
            List<Report> reports = reportService.getAllReports();
            for (Report report : reports) {
                if (report.getStateId() == 4) {
                    sendPhotoReport(update, report);
                }
            }
        }
    }

    public void getDefaultSwitchRealisation(Update update , Message message) throws InterruptedException, TelegramApiException {
        User user = userService.findByChatId(update.getMessage().getChatId());
        if (user.getStateID() < 3&& user.getChatId()==update.getMessage().getChatId()) {
            testReg(update);
        }
        List<CallVolunteerMsg> msgList = callVolunteerMsg.getAllCallVolunteerMsgs();
        for (CallVolunteerMsg msg : msgList) {
            if (msg.getStateId() == 1&& msg.getChatID()==update.getMessage().getChatId()) {
                msg.setMsgText(update.getMessage().getText());
                msg.setStateId(2);
                log.info("Вопрос " + msg);
                callVolunteerMsg.saveCallVolunteerMsg(msg);
                sendBotMessage(update.getMessage().getChatId(), "Ваш вопрос отправлен");
                sendBotMessage(update.getMessage().getChatId(), "С вами свяжутся в ближайшее время");
                Thread.sleep(1000);
                execute(keyboards.getBotStartUserMenu(update.getMessage().getChatId()));
            }
        }
        List<Report> reports = reportService.getAllReports();
        for (Report report : reports) {
            if (report.getStateId() < 4 && user.getChatId() == report.getChatId()) {
                sendReport(update, report);
            }
        }
        System.out.println(message.getText());
        System.out.println(message.getMessageId());
        log.info(update.getMessage().getChatId() + " " + message.getText());
    }

    public void getAllReportsFromBot(Update update) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "смотреть отчёты могут только волонтёры");
            Thread.sleep(1000);
            try {
                execute(keyboards.getBotStartUserMenu(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            getAllReports(update);
            try {
                execute(keyboards.getBotStartUserMenu(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void startBot(Update update) throws InterruptedException {
        String hello = EmojiParser.parseToUnicode(startMenu.sayHello());
        log.info(hello);
        sendBotMessage(update.getMessage().getChatId(), "Привет - Это Asha)");
        log.info(update.getMessage().getChatId() + " Привет - Это Asha)");
        sendPhoto(update.getMessage().getChatId());
        Thread.sleep(500);
        sendBotMessage(update.getMessage().getChatId(), hello);
        Thread.sleep(1200);
        try {
            execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void getAllReports(Update update) {
        List<Report> reports = reportService.getAllReports();
        for (Report report : reports) {
            sendBotMessage(update.getMessage().getChatId(), "Отчёт: " + report.getId());
            sendBotMessage(update.getMessage().getChatId(), "Дата: " + report.getMsgDate());
            sendBotMessage(update.getMessage().getChatId(),  report.getUserInfo());
            sendBotMessage(update.getMessage().getChatId(),  report.getDiet());
            sendBotMessage(update.getMessage().getChatId(),  report.getGeneralHealth());
            sendBotMessage(update.getMessage().getChatId(),  report.getBehaviorChange());
            sendPhotoFromByteCode(update.getMessage().getChatId(),report.getPhoto());
        }
    }

    public void sendPhotoReport(Update update,Report report) throws InterruptedException {
        report.setPhoto(getPhotoFromMessage(update));
        User user = userService.findByChatId(update.getMessage().getChatId());
        report.setUserInfo(user.toString());
        sendBotMessage(update.getMessage().getChatId(), "Отчёт отправлен");
        Thread.sleep(1200);
        report.setStateId(5);
        reportService.saveReport(report);
        try {
            execute(keyboards.getBotStartUserMenu(update.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

    }

    public void sendReportQuerry(Update update) throws InterruptedException {

       /* if (user.getDog() == null) {
            sendBotMessage(update.getMessage().getChatId(), "У вас нет собаки!!!");
            return;
        }*/
        Report report = new Report();
        report.setStateId(1);
        report.setMsgDate(Timestamp.valueOf(LocalDateTime.now()));
        report.setChatId(Math.toIntExact(update.getCallbackQuery().getMessage().getChatId()));
        reportService.saveReport(report);
        sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Мы рады, что вы забрали у нас собакена)))");
        Thread.sleep(800);
        sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Как поживает наш друг?");
        Thread.sleep(800);
        sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Какая диета у собаки?");
        Thread.sleep(800);

    }

    public void sendReport(Update update, Report report) throws InterruptedException {
        switch (report.getStateId()) {
            case 1:
                report.setDiet(update.getMessage().getText());
                sendBotMessage(update.getMessage().getChatId(), "Как самочувствие у питомца?");
                Thread.sleep(800);
                sendBotMessage(update.getMessage().getChatId(), "Есть ли жалобы на здоровье?");
                report.setStateId(2);
                reportService.saveReport(report);
                break;
            case 2:
                report.setGeneralHealth(update.getMessage().getText());
                sendBotMessage(update.getMessage().getChatId(), "Расскажите о поведении животного на новом месте");
                report.setStateId(3);
                reportService.saveReport(report);
                break;
            case 3:
                report.setBehaviorChange(update.getMessage().getText());
                sendBotMessage(update.getMessage().getChatId(), "Прикрепите фото питомца");
                report.setStateId(4);
                reportService.saveReport(report);
                break;
        }
    }

    private void askQuestion(Update update) {
        final long chatId = update.getCallbackQuery().getMessage().getChatId();
        MessageContext context = null;
        MessageState state;
        User user = userService.findByChatId(chatId);
        if (user == null) {
            sendBotMessage(chatId, "Задавать вопросы могут только зарегистрированные пользователи");
            return;
        }
        state = MessageState.getInitialState();
        CallVolunteerMsg msg = new CallVolunteerMsg(state.ordinal());
        msg.setMsgDate(Timestamp.valueOf(LocalDateTime.now()));
        msg.setChatID(chatId);
        msg.setEmail(user.getEmail());
        msg.setNumberPhone(user.getPhoneNumber());
        String text = update.getCallbackQuery().getMessage().getText();
        context = MessageContext.of(this, msg, text);
        state.enter(context);
        state.handleInput(context);
        callVolunteerMsg.saveCallVolunteerMsg(msg);
        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());
        msg.setStateId(state.ordinal());
        callVolunteerMsg.saveCallVolunteerMsg(msg);
    }

    private void testReg(Update update) {
        final String text = update.getMessage().getText();
        final long chatId = update.getMessage().getChatId();
        User user = userService.findByChatId(chatId);
        BotContext context;
        BotState state;
        if (user == null) {
            state = BotState.getInitialState();
            user = new User(update.getMessage().getChatId(), state.ordinal());
            userService.saveUser(user);
            context = BotContext.of(this, user, text);
            state.enter(context);
            log.info("New user registered: " + chatId);
            user.setFirstName(update.getMessage().getChat().getFirstName());
            user.setLastName(update.getMessage().getChat().getLastName());
        } else {
            context = BotContext.of(this, user, text);
            state = BotState.byId(user.getStateID());

            log.info("Update received for user in state: " + state);
        }
        state.handleInput(context);

        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());
        user.setStateID(state.ordinal());
        userService.saveUser(user);
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
    private byte[] getPhotoFromMessage(Update update) {
        try {
            if (update.getMessage().hasPhoto()) {
                long chatId = update.getMessage().getChatId();
                PhotoSize photo = imageParser.getLargestPhoto(update.getMessage().getPhoto());
                byte[] byteCode = imageParser.imageToByteCode(photo);
                //sendPhoto(chatId, byteCode);
                // Отправка данных будет производиться на сервер
                return byteCode;
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод отправляет фотографию в формате байтового кода пользователю в чате
     *
     * @param chatId   идентификатор чата
     * @param byteCode байтовый код фотографии
     */
    private void sendPhotoFromByteCode(long chatId, byte[] byteCode) {
        try {
            execute(imageParser.byteCodeToImage(chatId, byteCode));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getIfCallbackQuery(Update update) throws TelegramApiException, InterruptedException {
         if (update.hasCallbackQuery()) {
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
                EditMessageText messageText = new EditMessageText();
                messageText.setChatId(chatId);
                messageText.setMessageId((int) messageId);
                messageText.setText("Вас приветствует служба поддержки пользователей");
                execute(messageText);
                askQuestion(update);
            } else if (dataCallback.equals(WORK_TIME)) {
                getWorkTime(chatId, messageId);
            } else if (dataCallback.equals(ADDRESS)) {
                getContactUs(chatId, messageId);
            } else if (dataCallback.equals(SECURITY)) {
                getSafeInformation(chatId, messageId);
            } else if (dataCallback.equals(SEND_REPORT)) {
                User user = userService.findByChatId(update.getCallbackQuery().getMessage().getChatId());
                if (user == null) {
                    sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Отправлять отчёт могут только " +
                            "зарегистрированные пользователи!!!");
                }
                else {
                    EditMessageText messageText = new EditMessageText();
                    messageText.setChatId(chatId);
                    messageText.setMessageId((int) messageId);
                    messageText.setText("Отправить отчёт:");
                    execute(messageText);
                    try {
                        sendReportQuerry(update);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                }
            }
            else if (dataCallback.equals(DOG)) {
                EditMessageText messageText = new EditMessageText();
                messageText.setChatId(chatId);
                messageText.setMessageId((int) messageId);
                messageText.setText(EmojiParser.parseToUnicode("Добро пожаловать в приют для собак " + DOG_FACE));
                execute(messageText);
                Thread.sleep(800);
                execute(keyboards.getBotStartUserMenu(update.getCallbackQuery().getMessage().getChatId()));
            }
            else if (dataCallback.equals(CAT)) {
                EditMessageText messageText = new EditMessageText();
                messageText.setChatId(chatId);
                messageText.setMessageId((int) messageId);
                messageText.setText(EmojiParser.parseToUnicode("Добро пожаловать в приют для кошек " + CAT_FACE));
                execute(messageText);
                Thread.sleep(800);
                execute(keyboards.getBotStartUserMenu(update.getCallbackQuery().getMessage().getChatId()));
            } else if (dataCallback.equals(NECESSARY)) {
                execute(keyboards.WhatNeedToKnow(chatId, messageId));
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
}
