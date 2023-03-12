package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.config.Config;
import animal.shelter.animalsshelter.controllers.contexts.messagecontext.MessageContext;
import animal.shelter.animalsshelter.controllers.contexts.messagecontext.MessageState;
import animal.shelter.animalsshelter.controllers.contexts.usercontext.BotContext;
import animal.shelter.animalsshelter.controllers.contexts.usercontext.BotState;
import animal.shelter.animalsshelter.model.*;
import animal.shelter.animalsshelter.service.*;
import animal.shelter.animalsshelter.service.impl.ImageParserImpl;
import animal.shelter.animalsshelter.util.AdoptingDocumentsAndRules;
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
import java.util.*;
import java.util.stream.Collectors;

import static animal.shelter.animalsshelter.controllers.Keyboards.*;
import static animal.shelter.animalsshelter.util.Emoji.CAT_FACE;
import static animal.shelter.animalsshelter.util.Emoji.DOG_FACE;

@Log4j
@Component
public class TelegramBotStart extends TelegramLongPollingBot {
    public static final String TELL_ABOUT_SHELTER = "TELL_ME";
    public static final String WORK_TIME = "TIME_BUTTON";
    public static final String ADDRESS = "ADDRESS_BUTTON";
    public static final String SECURITY = "SECURITY_BUTTON";
    public static final String INFO_BUTTON = "INFO_BUTTON";
    public static final String NECESSARY = "NECESSARY_TO_GET_ANIMAL";
    public static final String SEND_REPORT = "SEND_REPORT";
    public static final String ADD_DOG = "ADD_DOG";
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
    private final DogService dogService;
    private final VolunteerService volunteerService;

    private final Keyboards keyboards = new Keyboards();

    private Dog.DogType dogType;

    private boolean shouldBreak = false;

    private void setShouldBreak(boolean flag) {
        this.shouldBreak = flag;
    }

    private boolean getShouldBreak() {
        return this.shouldBreak;
    }

    public void setDogType(String type) {
        switch (type.toLowerCase()) {
            case "puppy":
                this.dogType = Dog.DogType.PUPPY;
                break;
            case "adult":
                this.dogType = Dog.DogType.ADULT;
                break;
            case "disabled":
                this.dogType = Dog.DogType.DISABLED;
                break;
            default:
                throw new IllegalArgumentException("Неверный тип собаки: " + type);
        }
    }

    public Dog.DogType getDogType() {
        return this.dogType;
    }

    public TelegramBotStart(Config config,
                            UserService userService,
                            ReportService reportService,
                            CallVolunteerMsgService callVolunteerMsg,
                            DogService dogService,
                            VolunteerService volunteerService) {
        this.config = config;
        this.userService = userService;
        this.reportService = reportService;
        this.callVolunteerMsg = callVolunteerMsg;
        this.dogService = dogService;
        this.volunteerService = volunteerService;
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
            if (update.getMessage().getText().contains("/send")) {
                sendMessageFromAdminToBadUsersOfPets(update);
            }
            if (update.getMessage().getText().contains(update.getMessage().getText())
                    && !update.getMessage().getText().startsWith("/")
                    && Character.isDigit(update.getMessage().getText().charAt(0))) {
                answerToUser(update, update.getMessage().getText());
            }
            switch (message.getText()) {
                case "/start":
                    startBot(update);
                    break;
                case "/menu":
                    execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    break;
                case "/registration":
                    testReg(update);
                    break;
                case "/alldogs":
                    if (userService.findByChatId(update.getMessage().getChatId()) == null) {
                        sendBotMessage(update.getMessage().getChatId(), "Смотреть спивок собак" +
                                "могут только зарегистрированные пользователи");
                        execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    } else {
                        getAllDogs(update);
                    }
                case "/allquestions":
                    if (userService.findByChatId(update.getMessage().getChatId()) == null ||
                            userService.findByChatId(update.getMessage().getChatId()).isNotified() == false) {
                        sendBotMessage(update.getMessage().getChatId(), "Смотреть спивок вопросов " +
                                "могут только волонтёры!");
                        execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    }
                    if (userService.findByChatId(update.getMessage().getChatId()).isNotified() == true) {
                        List<CallVolunteerMsg> msgs = callVolunteerMsg.getAllCallVolunteerMsgs();
                        for (CallVolunteerMsg msg : msgs) {
                            sendBotMessage(update.getMessage().getChatId(), msg.toString());
                        }
                        execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    }
                    break;
                case "/allreports":
                    getAllReportsFromBot(update);
                    break;
                default:
                    getDefaultSwitchRealisation(update, message);
                    break;
            }
        }
        getIfCallbackQuery(update);
        if (update.hasMessage() && message.hasPhoto()) {
            List<Report> reports = reportService.getAllReports();
            for (Report report : reports) {
                if (report.getStateId() == 4 && report.getChatId() == update.getMessage().getChatId()) {
                    sendPhotoReport(update, report);
                }
            }

            List<Dog> dogs = dogService.getAllDogs();
            for (Dog dog : dogs) {
                if (dog.getStateId() == 10 && dog.getChatId() == update.getMessage().getChatId()) {
                    SendPhotoForDog(update, dog);
                }
            }
        }
    }

    public void answerToUser(Update update, String number) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            return;
        } else {
            String s = number.substring(0, number.indexOf(" "));
            long id = Long.parseLong(s);
            CallVolunteerMsg msg = callVolunteerMsg.getCallVolunteerMsgById(id);
            String messageText = update.getMessage().getText();
            String text = EmojiParser
                    .parseToUnicode(messageText.substring(messageText.indexOf(" ")));
            sendBotMessage(msg.getChatID(), "Вам ответил волонтёр: " + update.getMessage()
                    .getChat().getFirstName() + "\n" + text);
            callVolunteerMsg.deleteCallVolunteerMsg(id);
            try {
                execute(keyboards.getTypeOfShelter(msg.getChatID()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void sendDogQuery(Update update) throws InterruptedException, TelegramApiException {
        Dog dog = new Dog();
        dog.setStateId(1);
        dog.setChatId(Math.toIntExact(update.getCallbackQuery().getMessage().getChatId()));
        dogService.saveDog(dog);
        sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Вы решили добавить собакена?");
        Thread.sleep(800);
        execute(keyboards.getBackButtonForDog(update.getCallbackQuery().getMessage().getChatId(),
                "Как его зовут?"));
        Thread.sleep(800);
    }

    public void sendDog(Update update, Dog dog) throws InterruptedException, TelegramApiException {
        switch (dog.getStateId()) {
            case 1:
                dog.setNickname(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи правила знакомства с собакой"));
                dog.setStateId(2);
                dogService.saveDog(dog);
                break;
            case 2:
                dog.setIntroductionRules(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи требуемые документы"));
                dog.setStateId(3);
                dogService.saveDog(dog);
                break;
            case 3:
                dog.setRequiredDocuments(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи рекомендации по перевозке собаки"));
                dog.setStateId(4);
                dogService.saveDog(dog);
                break;
            case 4:
                dog.setTransportationRecommendations(update.getMessage().getText());
                execute(keyboards.getDogTypeButton(update.getMessage().getChatId(), "Укажите тип собаки"));
                dog.setStateId(5);
                dogService.saveDog(dog);
                break;
            case 5:
                dog.setDogType(getDogType());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи рекомендации по обустройству дома для собаки"));
                dog.setStateId(6);
                dogService.saveDog(dog);
                break;
            case 6:
                dog.setHomeArrangementRecommendations(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи основные рекомендации по коммуникации с собакой"));
                dog.setStateId(7);
                dogService.saveDog(dog);
                break;
            case 7:
                dog.setPrimaryCommunicationTips(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи рекомендованных кинологов"));
                dog.setStateId(8);
                dogService.saveDog(dog);
                break;
            case 8:
                dog.setRecommendedKynologists(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи причины отказа в выдаче собаки"));
                dog.setStateId(9);
                dogService.saveDog(dog);
                break;
            case 9:
                dog.setRefusalReasons(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Прикрепите Фото питомца"));
                dog.setStateId(10);
                dogService.saveDog(dog);
                break;
        }
    }

    public void sendMessageFromAdminToBadUsersOfPets(Update update) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "Писать напоминания об отчёте могут только волонтёры!!!");
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            String messageText = update.getMessage().getText();
            String text = EmojiParser
                    .parseToUnicode(messageText.substring(messageText.indexOf(" ")));
            long[] nums = reportService.getAllReports()
                    .stream()
                    .mapToLong(Report::getChatId)
                    .distinct()
                    .toArray();
            for (long num : nums) {
                sendBotMessage(num, text);
            }
        }
    }

    public void getDefaultSwitchRealisation(Update update, Message message) throws InterruptedException, TelegramApiException {
        User user = userService.findByChatId(update.getMessage().getChatId());
        if (user.getStateID() < 3 && user.getChatId() == update.getMessage().getChatId()) {
            testReg(update);
        }
        List<CallVolunteerMsg> msgList = callVolunteerMsg.getAllCallVolunteerMsgs();
        for (CallVolunteerMsg msg : msgList) {
            if (msg.getStateId() == 1 && msg.getChatID() == update.getMessage().getChatId()) {
                msg.setMsgText(update.getMessage().getText());
                msg.setStateId(2);
                log.info("Вопрос " + msg);
                callVolunteerMsg.saveCallVolunteerMsg(msg);
                sendBotMessage(update.getMessage().getChatId(), "Ваш вопрос отправлен");
                sendBotMessage(update.getMessage().getChatId(), "С вами свяжутся в ближайшее время");
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            }
        }

        List<Report> reports = reportService.getAllReports();
        for (Report report : reports) {
            if (report.getStateId() < 4 && report.getChatId() == update.getMessage().getChatId()) {
                sendReport(update, report);
            }
        }

        List<Dog> dogs = dogService.getAllDogs();
        for (Dog dog : dogs) {
            if (dog.getStateId() < 10 && dog.getChatId() == update.getMessage().getChatId()) {
                sendDog(update, dog);
            }
        }
        System.out.println(message.getText());
        System.out.println(message.getMessageId());
        log.info(update.getMessage().getChatId() + " " + message.getText());
    }

    public void sendQuestionsToAdmins(CallVolunteerMsg msg) {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.isNotified() == true) {
                sendBotMessage(user.getChatId(), msg.toString());
            }
        }
    }

    public void getAllReportsFromBot(Update update) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "смотреть отчёты могут только волонтёры");
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            getAllReports(update);
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
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
        sendBotMessage(update.getMessage().getChatId(), hello);
        try {
            execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void getAllDogs(Update update) {

        List<Dog> dogs = dogService.getAllDogs();
        for (Dog dog : dogs) {
            sendBotMessage(update.getMessage().getChatId(), "Собака: " + dog.getId() + "\n" + dog.getNickname() + "\n" +
                    dog.getDogType() + "\n" +
                    dog.getHomeArrangementRecommendations() + "\n" +
                    dog.getRecommendedKynologists() + "\n" +
                    dog.getIntroductionRules() + "\n" +
                    dog.getPrimaryCommunicationTips() + "\n" +
                    dog.getRefusalReasons() + "\n" +
                    dog.getRequiredDocuments() + "\n" +
                    dog.getTransportationRecommendations());
            sendPhotoFromByteCode(update.getMessage().getChatId(), dog.getDogPhoto());
        }
    }

    public void getAllReports(Update update) {
        List<Report> reports = reportService.getAllReports();
        for (Report report : reports) {
            sendBotMessage(update.getMessage().getChatId(), "Отчёт: " + report.getId());
            sendBotMessage(update.getMessage().getChatId(), "Дата: " + report.getMsgDate());
            sendBotMessage(update.getMessage().getChatId(), report.getUserInfo());
            sendBotMessage(update.getMessage().getChatId(), report.getDiet());
            sendBotMessage(update.getMessage().getChatId(), report.getGeneralHealth());
            sendBotMessage(update.getMessage().getChatId(), report.getBehaviorChange());
            sendPhotoFromByteCode(update.getMessage().getChatId(), report.getPhoto());
        }
    }

    public void sendPhotoReport(Update update, Report report) throws InterruptedException {
        report.setPhoto(getPhotoFromMessage(update));
        User user = userService.findByChatId(update.getMessage().getChatId());
        report.setUserInfo(user.toString());
        sendBotMessage(update.getMessage().getChatId(), "Отчёт отправлен");
        report.setStateId(5);
        reportService.saveReport(report);
        try {
            execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

    }

    public void SendPhotoForDog(Update update, Dog dog) throws InterruptedException {
        dog.setDogPhoto(getPhotoFromMessage(update));
        sendBotMessage(update.getMessage().getChatId(), "Собака создана");
        Thread.sleep(1500);
        dog.setStateId(11);
        dogService.saveDog(dog);
        try {
            execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void sendReportQuery(Update update) throws InterruptedException, TelegramApiException {

        Report report = new Report();
        report.setStateId(1);
        report.setMsgDate(Timestamp.valueOf(LocalDateTime.now()));
        report.setChatId(Math.toIntExact(update.getCallbackQuery().getMessage().getChatId()));
        reportService.saveReport(report);
        sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Мы рады, что вы забрали у нас питомца)))");
        sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Как поживает наш друг?");
        execute(keyboards.getBackButtonForReport(update.getCallbackQuery().getMessage().getChatId(),
                "Какая диета у питомца?"));
    }

    public void sendReport(Update update, Report report) throws InterruptedException, TelegramApiException {
        switch (report.getStateId()) {
            case 1:
                report.setDiet(update.getMessage().getText());
                sendBotMessage(update.getMessage().getChatId(), "Как самочувствие у питомца?");
                execute(keyboards.getBackButtonForReport(update.getMessage().getChatId(),
                        "Есть ли жалобы на здоровье?"));
                report.setStateId(2);
                reportService.saveReport(report);
                break;
            case 2:
                report.setGeneralHealth(update.getMessage().getText());
                execute(keyboards.getBackButtonForReport(update.getMessage().getChatId(),
                        "Расскажите о поведении животного на новом месте"));
                report.setStateId(3);
                reportService.saveReport(report);
                break;
            case 3:
                report.setBehaviorChange(update.getMessage().getText());
                execute(keyboards.getBackButtonForReport(update.getMessage().getChatId(),
                        "Прикрепите фото питомца"));
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
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton shelterInfoButton = new InlineKeyboardButton();
        shelterInfoButton.setText("Информация о приюте");
        shelterInfoButton.setCallbackData(INFO_BUTTON);
        InlineKeyboardButton necessary = new InlineKeyboardButton();
        necessary.setText("Хотите собаку? Важно знать!");
        necessary.setCallbackData(NECESSARY);
        InlineKeyboardButton report = new InlineKeyboardButton();
        report.setText("Отправить отчёт о животном");
        report.setCallbackData(SEND_REPORT);
        InlineKeyboardButton call = new InlineKeyboardButton();
        call.setText("Вопрос к волонтёру");
        call.setCallbackData(CALL_VOLUNTEER);
        InlineKeyboardButton addDog = new InlineKeyboardButton();
        addDog.setText("Добавить собаку");
        addDog.setCallbackData(ADD_DOG);
        row.add(shelterInfoButton);
        row1.add(necessary);
        row2.add(report);
        row3.add(call);
        row4.add(addDog);
        rows.add(row);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
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
                PhotoSize photo = imageParser.getLargestPhoto(update.getMessage().getPhoto());
                byte[] byteCode = imageParser.imageToByteCode(photo);
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

    public void getSampleReport(long chatId, long messageID, Update update) throws TelegramApiException, InterruptedException {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageID);
        Report report = reportService.getReportById(10);
        messageText.setText("Образец и правило отчёта \n" + EmojiParser.parseToUnicode(
                Emoji.CHECK_MARK + " Отчёт нужно заполнять и отправлять ежедневно. \n"
                        + Emoji.CHECK_MARK + " Если не отправлять отчёт два дня, придёт уведомление. \n"
                        + Emoji.CHECK_MARK + " При систематическом пропуске отчётности волонтёры вправе вернуть животное в приют\n\n "
                        + Emoji.QUESTION_MARK + " Какая диета у собаки?\n"
                        + Emoji.DOUBLE_BANG_MARK + " " + report.getDiet() + "\n"
                        + Emoji.QUESTION_MARK + " Как самочувствие у питомца? Есть ли жалобы на здоровье? \n"
                        + Emoji.DOUBLE_BANG_MARK + " " + report.getGeneralHealth() + "\n"
                        + Emoji.QUESTION_MARK + " Расскажите о поведении животного на новом месте \n"
                        + Emoji.DOUBLE_BANG_MARK + " " + report.getBehaviorChange() + "\n"
                        + Emoji.QUESTION_MARK + " Прикрепите фото питомца")
        );
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        sendPhotoFromByteCode(update.getCallbackQuery().getMessage().getChatId(), report.getPhoto());
        execute(keyboards.getTypeOfShelter(update.getCallbackQuery().getMessage().getChatId()));
    }

    public void getSampleReportCat(long chatId, long messageID, Update update) throws TelegramApiException, InterruptedException {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(chatId);
        messageText.setMessageId((int) messageID);
        Report report = reportService.getReportById(54);
        messageText.setText("Образец и правило отчёта \n" + EmojiParser.parseToUnicode(
                Emoji.CHECK_MARK + " Отчёт нужно заполнять и отправлять ежедневно. \n"
                        + Emoji.CHECK_MARK + " Если не отправлять отчёт два дня, придёт уведомление. \n"
                        + Emoji.CHECK_MARK + " При систематическом пропуске отчётности волонтёры вправе вернуть животное в приют\n\n "
                        + Emoji.QUESTION_MARK + " Какая диета у Кошки?\n"
                        + Emoji.DOUBLE_BANG_MARK + " " + report.getDiet() + "\n"
                        + Emoji.QUESTION_MARK + " Как самочувствие у питомца? Есть ли жалобы на здоровье? \n"
                        + Emoji.DOUBLE_BANG_MARK + " " + report.getGeneralHealth() + "\n"
                        + Emoji.QUESTION_MARK + " Расскажите о поведении животного на новом месте \n"
                        + Emoji.DOUBLE_BANG_MARK + " " + report.getBehaviorChange() + "\n"
                        + Emoji.QUESTION_MARK + " Прикрепите фото питомца")
        );
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        sendPhotoFromByteCode(update.getCallbackQuery().getMessage().getChatId(), report.getPhoto());
        execute(keyboards.getTypeOfShelter(update.getCallbackQuery().getMessage().getChatId()));
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
            } else if (dataCallback.equals(BACK_REPORT)) {
                List<Report> reportList = reportService.getAllReports();
                List<Report> reports = reportList
                        .stream()
                        .filter(report -> report.getChatId() == update.getCallbackQuery().getMessage().getChatId())
                        .filter(report -> report.getStateId() < 5)
                        .collect(Collectors.toList());
                reportService.deleteReport(reports.get(reports.size() - 1).getId());
                execute(keyboards.getTypeOfShelter(chatId));
            } else if (dataCallback.equals(BACK_ADD_DOG)) {
                List<Dog> dogList = dogService.getAllDogs();
                List<Dog> dogs = dogList
                        .stream()
                        .filter(dog -> dog.getChatId() == update.getCallbackQuery().getMessage().getChatId())
                        .filter(dog -> dog.getStateId() < 11)
                        .collect(Collectors.toList());
                    dogService.deleteDog(dogs.get(dogs.size() - 1).getId());
                    execute(keyboards.getTypeOfShelter(chatId));


            } else if (dataCallback.equals(BACK_QUESTION)) {
                List<CallVolunteerMsg> msgList = callVolunteerMsg.getAllCallVolunteerMsgs();
                List<CallVolunteerMsg> msgs = msgList
                        .stream()
                        .filter(msg -> msg.getChatID() == update.getCallbackQuery().getMessage().getChatId())
                        .collect(Collectors.toList());
                CallVolunteerMsg msg1 = msgs.get(msgs.size() - 1);
                if (msg1.getStateId() < 2 && msg1.getChatID() == update.getCallbackQuery().getMessage().getChatId()) {
                    callVolunteerMsg.deleteCallVolunteerMsg(msg1.getId());
                    execute(keyboards.getTypeOfShelter(chatId));
                }
            } else if (dataCallback.equals(WORK_TIME)) {
                getWorkTime(chatId, messageId);
            } else if (dataCallback.equals(SAMPLE_REPORT_CAT)) {
                getSampleReportCat(chatId, messageId, update);
            } else if (dataCallback.equals(ADDRESS)) {
                getContactUs(chatId, messageId);
            } else if (dataCallback.equals(SECURITY)) {
                getSafeInformation(chatId, messageId);
            } else if (dataCallback.equals(SEND_REPORT)) {
                User user = userService.findByChatId(update.getCallbackQuery().getMessage().getChatId());
                if (user == null) {
                    sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Отправлять отчёт могут только " +
                            "зарегистрированные пользователи!!!");
                } else {
                    EditMessageText messageText = new EditMessageText();
                    messageText.setChatId(chatId);
                    messageText.setMessageId((int) messageId);
                    messageText.setText("Отправить отчёт:");
                    execute(messageText);
                    try {
                        sendReportQuery(update);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                }
            } else if (dataCallback.equals(DOG)) {
                EditMessageText messageText = new EditMessageText();
                messageText.setChatId(chatId);
                messageText.setMessageId((int) messageId);
                messageText.setText(EmojiParser.parseToUnicode("Добро пожаловать в приют для собак " + DOG_FACE));
                execute(messageText);
                execute(keyboards.getBotStartUserMenu(update.getCallbackQuery().getMessage().getChatId()));
            } else if (dataCallback.equals(CAT)) {
                EditMessageText messageText = new EditMessageText();
                messageText.setChatId(chatId);
                messageText.setMessageId((int) messageId);
                messageText.setText(EmojiParser.parseToUnicode("Добро пожаловать в приют для кошек " + CAT_FACE));
                execute(messageText);
                execute(keyboards.getBotStartUserMenuCat(update.getCallbackQuery().getMessage().getChatId()));
            } else if (dataCallback.equals(ADD_DOG)) {
                //Volunteer volunteer = volunteerService.findByChatId(update.getCallbackQuery().getMessage().getChatId());
                //if (volunteer == null) {
                //    sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Отправлять отчёт могут только " +
                //            "волонтеры!!");
                //} else {

                if (userService.findByChatId(update.getCallbackQuery().getMessage().getChatId()).isNotified() == true) {
                    EditMessageText messageText = new EditMessageText();
                    messageText.setChatId(chatId);
                    messageText.setMessageId((int) messageId);
                    messageText.setText("Добавление собаки:");
                    execute(messageText);
                    try {
                        sendDogQuery(update);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                }
                if (userService.findByChatId(update.getCallbackQuery().getMessage().getChatId()) == null
                        || userService.findByChatId(update.getCallbackQuery().getMessage().getChatId()).isNotified() == false) {
                    sendBotMessage(chatId,
                            "Добавлять собак могут только волонтёры ");
                    execute(keyboards.getBotStartUserMenu(chatId));
                }

                //}
            } else if (dataCallback.equals(PUPPY_TYPE)) {
                setDogType(PUPPY_TYPE);
                sendBotMessage(chatId, "Вы выбрали - щенка. \n" +
                        "Для продолжения напишите что-нибудь в чат");
                setShouldBreak(true);
            } else if (dataCallback.equals(ADULT_TYPE)) {
                setDogType(ADULT_TYPE);
                sendBotMessage(chatId, "Вы выбрали - взрослую собаку. \n" +
                        "Для продолжения напишите что-нибудь в чат");
                setShouldBreak(true);
            } else if (dataCallback.equals(DISABLED_TYPE)) {
                setDogType(DISABLED_TYPE);
                sendBotMessage(chatId, "Вы выбрали - собаку с ограниченными возможностями. \n" +
                        "Для продолжения напишите что-нибудь в чат");
                setShouldBreak(true);
            } else if (dataCallback.equals(NECESSARY)) {
                execute(keyboards.WhatNeedToKnow(chatId, messageId));
            } else if (dataCallback.equals(SAMPLE_REPORT)) {
                getSampleReport(chatId, messageId, update);
            } else if (dataCallback.equals(BACK_TWO)) {
                execute(keyboards.WhatNeedToKnow(chatId, messageId));
            } else if (dataCallback.equals(ACQUAINTANCE)) {
                execute(keyboards.getWindowOne(chatId, messageId));
            } else if (dataCallback.equals(DOC_CAT)) {
                execute(keyboards.messageTextCatTwo(chatId, messageId, AdoptingDocumentsAndRules.DOCUMENTS_FOR_ADOPTION));
            } else if (dataCallback.equals(HOME_PREPARATION_KITTEN)) {
                execute(keyboards.messageTextCatTwo(chatId, messageId, AdoptingDocumentsAndRules.DOCUMENTS_FOR_ADOPTION));
            } else if (dataCallback.equals(TRAVEL_CAT)) {
                execute(keyboards.messageTextCatTwo(chatId, messageId, AdoptingDocumentsAndRules.TRAVELLING_WITH_CAT));
            } else if (dataCallback.equals(HOME_PREPARATION_CAT)) {
                execute(keyboards.messageTextCatTwo(chatId, messageId, AdoptingDocumentsAndRules.PLACE_FOR_CAT));
            } else if (dataCallback.equals(REASONS_CAT)) {
                execute(keyboards.messageTextCatTwo(chatId, messageId, AdoptingDocumentsAndRules.ADOPTING_DENY_REASONS));
            } else if (dataCallback.equals(HOME_PREPARATION_INVALID_CAT)) {
                execute(keyboards.messageTextCatTwo(chatId, messageId, AdoptingDocumentsAndRules.PLACE_FOR_DISABLED_CAT));
            } else if (dataCallback.equals(ACQUAINTANCE_CAT)) {
                execute(keyboards.messageTextCatTwo(chatId, messageId, AdoptingDocumentsAndRules.MEETING_CAT_RULES));
            } else if (dataCallback.equals(DOCUMENTS)) {
                execute(keyboards.getWindowTwo(chatId, messageId));
            } else if (dataCallback.equals(TRAVEL)) {
                execute(keyboards.getWindowThree(chatId, messageId));
            } else if (dataCallback.equals(HOME_PREPARATION_PUPPY)) {
                execute(keyboards.getWindowFour(chatId, messageId));
            } else if (dataCallback.equals(HOME_PREPARATION_DOG)) {
                execute(keyboards.getWindowFive(chatId, messageId));
            } else if (dataCallback.equals(HOME_PREPARATION_INVALID_DOG)) {
                execute(keyboards.getWindowSix(chatId, messageId));
            } else if (dataCallback.equals(TIPS_FROM_HANDLER)) {
                execute(keyboards.getWindowSeven(chatId, messageId));
            } else if (dataCallback.equals(CONTACT_HANDLER)) {
                execute(keyboards.getWindowEight(chatId, messageId));
            } else if (dataCallback.equals(REASONS)) {
                execute(keyboards.getWindowNine(chatId, messageId));
            } else if (dataCallback.equals(GO_BACK_CAT)) {
                execute(keyboards.getAboutShelterCat(chatId, messageId));
            } else if (dataCallback.equals(INFO_BUTTON_CAT)) {
                execute(keyboards.getAboutShelterCat(chatId, messageId));
            } else if (dataCallback.equals(NECESSARY_CAT)) {
                execute(keyboards.WhatNeedToKnowAboutCat(chatId, messageId));
            } else if (dataCallback.equals(BACK_CAT_TWO)) {
                execute(keyboards.WhatNeedToKnowAboutCat(chatId, messageId));
            } else if (dataCallback.equals(BACK_CAT_ONE)) {
                execute(keyboards.getBotStartUserMenuCatBack(chatId, messageId));
            } else if (dataCallback.equals(TELL_ABOUT_SHELTER_CAT)) {
                execute(keyboards.messageTextCatOne(chatId, messageId, startMenu.getInfoAboutShelter()));
            } else if (dataCallback.equals(WORK_TIME_CAT)) {
                execute(keyboards.messageTextCatOne(chatId, messageId, startMenu.workTime()));
            } else if (dataCallback.equals(ADDRESS_CAT)) {
                execute(keyboards.messageTextCatOne(chatId, messageId, startMenu.contactUs()));
            } else if (dataCallback.equals(SECURITY_CAT)) {
                execute(keyboards.messageTextCatOne(chatId, messageId, startMenu.toBeSafeRegulations()));
            }
        }
    }
}
