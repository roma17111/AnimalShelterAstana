package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.config.Config;
import animal.shelter.animalsshelter.controllers.contexts.messagecontext.MessageContext;
import animal.shelter.animalsshelter.controllers.contexts.messagecontext.MessageState;
import animal.shelter.animalsshelter.controllers.contexts.usercontext.BotContext;
import animal.shelter.animalsshelter.controllers.contexts.usercontext.BotState;
import animal.shelter.animalsshelter.controllers.notification.DailyNotification;
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
import static animal.shelter.animalsshelter.controllers.Keyboards.CAT;
import static animal.shelter.animalsshelter.controllers.Keyboards.DOG;
import static animal.shelter.animalsshelter.util.Emoji.*;

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
    public static final String URL_START_PHOTO = "root/images/msg6162958373-22385.jpg";

    public static final String ADMIN_COMMANDS = "Команды волонтёров: \n\n " +
            "/users - посмотреть список пользователей \n" +
            "/send - Отправить сообщение о плохом отчёте всем пользователям, " +
            "у которых текст сообщений в отчётах очень короткий. Отчёты нухно писать подробно. \n" +
            "/allquestions - Посмотреть список вопросов от пользователей\n" +
            "/allreports - Посмотреть список отчётов от усыновителей питомцев\n" +
            "/badreport{id} - отправить шаблон сообщения о конкретном отчёте - плохой отчёт.\n" +
            "id - id отчёта из базы данных.\n" +
            "Пример: /badreport 1\n" +
            "/message{id} - Отправить сообщение пользователю из базы по id\n" +
            "Пример: /message12 Привет. Как дела?\n" +
            "/answer{id} - Ответить пользователю по id оставленного вопроса из базы\n" +
            "Пример: /answer12 Пока этой породы нет.\n" +
            "/adddog - Добавить собаку\n" +
            "/addcat - Добавить кошку/кота\n" +
            "/congratulations {id} Поздравить усыновителя с успешным прохождением испытательного срока\n" +
            "Где id - id отчёта из БД\n" +
            "пример: /congratulations 1 \n" +
            "/continue14 {id} - Продлить испытательный срок на 14 дней \n" +
            "Где id - id отчёта из БД\n" +
            "Пример: /continue14 25\n" +
            "/continue30 {id} - Продлить испытательный срок на 30 дней \n" +
            "Где id - id отчёта из БД\n" +
            "Пример: /continue30 25\n" +
            "/takepet {id} - Отправить сообщение об изъятии собаки у усыновителя \n" +
            "Где id - id отчёта из БД\n" +
            "Пример: /takepet 25";
    private final Config config;
    private final StartMenu startMenu = new StartMenu();
    private final ImageParser imageParser = new ImageParserImpl(this);
    private final UserService userService;
    private final ReportService reportService;
    private final DogService dogService;

    private final CatService catService;
    private final VolunteerService volunteerService;
    private final CallVolunteerMsgService callVolunteerMsg;
    private Dog.DogType dogType;
    private Cat.CatType catType;

    private final Keyboards keyboards = new Keyboards();

    /**
     * Устанавливает тип собаки.
     *
     * @param type строка, указывающая тип собаки ("puppy" - щенок, "adult" - взрослая, "disabled" - с ограниченными возможностями)
     * @throws IllegalArgumentException если переданный тип собаки не соответствует одному из трех возможных значений
     */
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

    /**
     * Устанавливает тип кошки.
     *
     * @param type строка, указывающая тип собаки ("puppy" - котенок, "adult" - взрослый, "disabled" - с ограниченными возможностями)
     * @throws IllegalArgumentException если переданный тип кошки не соответствует одному из трех возможных значений
     */
    public void setCatType(String type) {
        switch (type.toLowerCase()) {
            case "kitten":
                this.catType = Cat.CatType.KITTEN;
                break;
            case "cat_cat":
                this.catType = Cat.CatType.CAT_CAT;
                break;
            case "disabled_cat":
                this.catType = Cat.CatType.DISABLED_CAT;
                break;
            default:
                throw new IllegalArgumentException("Неверный тип Кошки: " + type);
        }
    }

    public Dog.DogType getDogType() {
        return this.dogType;
    }

    public Cat.CatType getCatType() {
        return catType;
    }

    public TelegramBotStart(Config config,
                            UserService userService,
                            ReportService reportService,
                            CallVolunteerMsgService callVolunteerMsg,
                            DogService dogService,
                            CatService catService,
                            VolunteerService volunteerService) {
        this.config = config;
        this.userService = userService;
        this.reportService = reportService;
        this.callVolunteerMsg = callVolunteerMsg;
        this.dogService = dogService;
        this.catService = catService;
        this.volunteerService = volunteerService;

        DailyNotification dailyNotification = new DailyNotification(this, this.userService, this.reportService);
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
            if (update.getMessage().getText().contains("/congratulations")) {
                sendCongratulation(update);
            }
            if (update.getMessage().getText().contains("/continue14")) {
                sendDay14(update);
            }
            if (update.getMessage().getText().contains("/continue30")) {
                sendDay30(update);
            }
            if (update.getMessage().getText().contains("/takepet")) {
                sendTakePet(update);
            }
            if (update.getMessage().getText().contains("/message")) {
                sendMessageToUser(update);
            }
            if (update.getMessage().getText().contains("/badreport")) {
                sendMessageAboutBadReport(update);
            }
            if (update.getMessage().getText().contains("/answer")) {
                answerToUser(update, update.getMessage().getText());
            }
            switch (message.getText()) {
                case "/start":
                    startBot(update);
                    break;
                case "/admin":
                    if (userService.findByChatId(update.getMessage().getChatId()) == null ||
                            userService.findByChatId(update.getMessage().getChatId()).isNotified() == false) {
                        sendBotMessage(update.getMessage().getChatId(), "Смотреть команды администраторов" +
                                "могут только волонтёры!");
                        execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    }
                    if (userService.findByChatId(update.getMessage().getChatId()).isNotified() == true) {
                        sendBotMessage(update.getMessage().getChatId(), ADMIN_COMMANDS);
                        execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    }

                    break;
                case "/menu":
                    execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    break;
                case "/registration":
                    testReg(update);
                    break;
                case "/users":
                    if (userService.findByChatId(update.getMessage().getChatId()) == null ||
                            userService.findByChatId(update.getMessage().getChatId()).isNotified() == false) {
                        sendBotMessage(update.getMessage().getChatId(), "Смотреть список пользователей" +
                                "могут только волонтёры!");
                        execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    }
                    if (userService.findByChatId(update.getMessage().getChatId()).isNotified() == true) {
                        List<User> userList = userService.getAllUsers();
                        for (User user : userList) {
                            sendBotMessage(update.getMessage().getChatId(), user.toString());
                        }
                        execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
                    }
                    break;
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
                case "/adddog":
                    if (userService.findByChatId(update.getMessage().getChatId()).isNotified() == true) {
                        SendMessage messageText = new SendMessage();
                        messageText.setChatId(update.getMessage().getChatId());
                        messageText.setText("Добавление собаки:");
                        execute(messageText);
                        try {
                            sendDogQuery(update);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage());
                        }
                    }
                    if (userService.findByChatId(update.getMessage().getChatId()) == null
                            || userService.findByChatId(update.getMessage().getChatId()).isNotified() == false) {
                        sendBotMessage(update.getMessage().getChatId(),
                                "Добавлять собак могут только волонтёры ");
                    }
                    break;
                case "/addcat":
                    if (userService.findByChatId(update.getMessage().getChatId()).isNotified() == true) {
                        SendMessage messageText = new SendMessage();
                        messageText.setChatId(update.getMessage().getChatId());
                        messageText.setText("Добавление кота/кошки:");
                        execute(messageText);
                        sendCatQuerry(update);
                    }
                    if (userService.findByChatId(update.getMessage().getChatId()) == null
                            || userService.findByChatId(update.getMessage().getChatId()).isNotified() == false) {
                        sendBotMessage(update.getMessage().getChatId(),
                                "Добавлять Котов могут только волонтёры ");
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
                if (dog.getStateId() == 6 && dog.getChatId() == update.getMessage().getChatId()) {
                    sendPhotoForDog(update, dog);
                }
            }
            List<Cat> cats = catService.findAllCats();
            for (Cat cat : cats) {
                if (cat.getStateId() == 6 && cat.getChatId() == update.getMessage().getChatId()) {
                    sendPhotoForCat(update, cat);
                }
            }
        }
    }

    /**
     * Отвечает пользователю на вызов волонтера, используя объект Update и номер вызова.
     * Если пользователь не найден или не уведомлен, метод ничего не делает.
     *
     * @param update Объект Update, содержащий информацию о сообщении.
     * @param number Номер вызова волонтера.
     * @throws InterruptedException если поток выполнения был прерван.
     */
    public void answerToUser(Update update, String number) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            return;
        } else {
            String messageText = update.getMessage().getText();
            String text = messageText.substring(7, messageText.indexOf(" "));
            int id = Integer.parseInt(text);
            CallVolunteerMsg msg = callVolunteerMsg.getCallVolunteerMsgById((long) id);
            String message = messageText.substring(messageText.indexOf(" "));
            sendBotMessage(msg.getChatID(), "Вам ответил волонтёр: " + update.getMessage()
                    .getChat().getFirstName() + "\n" + message);
            callVolunteerMsg.deleteCallVolunteerMsg((long) id);
            try {
                execute(keyboards.getMenuAfterAnswer(msg.getChatID()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Метод для отправки запроса на добавление кота и создания записи в базе данных.
     *
     * @param update Объект класса Update, содержащий данные о сообщении.
     */
    public void sendCatQuerry(Update update) {
        Cat cat = new Cat();
        cat.setChatId(Math.toIntExact(update.getMessage().getChatId()));
        cat.setStateId(1);
        catService.saveCat(cat);
        sendBotMessage(update.getMessage().getChatId(), "Вы решили добавить Кошкатуна?");
        try {
            execute(keyboards.getBackButtonForCat(update.getMessage().getChatId(),
                    "Как зовут этого славного пушистого красавца?"));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Метод для отправки запроса на добавление собаки и создания записи в базе данных.
     *
     * @param update Объект класса Update, содержащий данные о сообщении.
     */
    public void sendDogQuery(Update update) throws InterruptedException, TelegramApiException {
        Dog dog = new Dog();
        dog.setChatId(Math.toIntExact(update.getMessage().getChatId()));
        dog.setStateId(1);
        dogService.saveDog(dog);
        sendBotMessage(update.getMessage().getChatId(), "Вы решили добавить собакена?");
        execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                "Как его зовут?"));
    }

    /**
     * Метод отправляет сообщение и обрабатывает состояние добавления новой собаки.
     * В зависимости от текущего состояния, метод запрашивает у пользователя информацию о собаке:
     * кличку, возраст, породу, тип собаки, описание и фото.
     * После получения всей необходимой информации, метод сохраняет собаку в базу данных.
     *
     * @param update обновление из Telegram API
     * @param dog объект класса Dog, представляющий добавляемую собаку
     * @throws InterruptedException исключение, возникающее, когда процесс исполнения прерывается
     * @throws TelegramApiException исключение, возникающее при ошибке выполнения Telegram API
     */
    public void sendDog(Update update, Dog dog) throws InterruptedException, TelegramApiException {
        switch (dog.getStateId()) {
            case 1:
                dog.setNickname(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи возраст кошки"));
                dog.setStateId(2);
                dogService.saveDog(dog);
                break;
            case 2:
                dog.setAge(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Укажи породу"));
                dog.setStateId(3);
                dogService.saveDog(dog);
                break;

            case 3:
                dog.setBreed(update.getMessage().getText());
                execute(keyboards.getDogTypeButton(update.getMessage().getChatId(), "Выберите тип собаки"));
                dog.setStateId(4);
                dogService.saveDog(dog);
                break;
            case 4:
                dog.setDogType(getDogType());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Опишите поведение и характер собаки"));
                dog.setStateId(5);
                dogService.saveDog(dog);
                break;
            case 5:
                dog.setDescription(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Прикрепите фото питомца"));
                dog.setStateId(6);
                dogService.saveDog(dog);
                break;

        }
    }

    /**
     * Метод отправляет сообщение и обрабатывает состояние добавления новой кошки.
     * В зависимости от текущего состояния, метод запрашивает у пользователя информацию о кошке:
     * кличку, возраст, породу, тип кошки, описание и фото.
     * После получения всей необходимой информации, метод сохраняет собаку в базу данных.
     *
     * @param update обновление из Telegram API
     * @param cat объект класса Dog, представляющий добавляемую собаку
     * @throws InterruptedException исключение, возникающее, когда процесс исполнения прерывается
     * @throws TelegramApiException исключение, возникающее при ошибке выполнения Telegram API
     */
    public void sendCat(Update update, Cat cat) throws InterruptedException, TelegramApiException {
        switch (cat.getStateId()) {
            case 1:
                cat.setName(update.getMessage().getText());
                execute(keyboards.getBackButtonForCat(update.getMessage().getChatId(),
                        "Укажите возраст"));
                cat.setStateId(2);
                catService.saveCat(cat);
                break;
            case 2:
                cat.setAge(update.getMessage().getText());
                execute(keyboards.getBackButtonForCat(update.getMessage().getChatId(),
                        "Укажите породу"));
                cat.setStateId(3);
                catService.saveCat(cat);
                break;
            case 3:
                cat.setBreed(update.getMessage().getText());
                execute(keyboards.getCatTypeButton(update.getMessage().getChatId(), "Выберите тип кошки"));
                cat.setStateId(4);
                catService.saveCat(cat);
                break;
            case 4:
                cat.setCatType(getCatType());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Опишите привычки и характер кошки/кота"));
                cat.setStateId(5);
                catService.saveCat(cat);
                break;
            case 5:
                cat.setDescription(update.getMessage().getText());
                execute(keyboards.getBackButtonForDog(update.getMessage().getChatId(),
                        "Прикрепите Фото кошки/кота"));
                cat.setStateId(6);
                catService.saveCat(cat);
                break;
        }
    }

    /**
     * Отправляет сообщение от администратора неблагополучным пользователям питомцев. Если пользователь не является волонтером или не был уведомлен,
     * то отправляется сообщение с информацией о том, что писать напоминания об отчёте могут только волонтёры, и пользователь возвращается на выбор типа приюта.
     * Если пользователь является волонтером, то формируется сообщение о том, что пользователь не заполнил отчёт подробно и призывается ответственнее подходить к этому занятию.
     * В случае несоблюдения правил волонтеры приюта будут обязаны самолично проверять условия содержания животного.
     *
     * @param update Обновление, содержащее информацию о сообщении
     * @throws InterruptedException в случае, если поток был прерван во время выполнения операции
     */
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
            String messageText = "Дорогой усыновитель," +
                    Emoji.CONFUSED_FACE + " Мы заметили, что ты заполняешь отчет не так подробно," +
                    " как необходимо. Пожалуйста, подойди ответственнее к этому" +
                    " занятию. В противном случае волонтеры приюта будут" +
                    " обязаны самолично проверять условия содержания животного";
            String text = EmojiParser
                    .parseToUnicode(messageText.substring(messageText.indexOf(" ")));
            long[] nums = reportService.getAllReports()
                    .stream()
                    .filter(report -> report.getDiet().length() < 10)
                    .filter(report -> report.getBehaviorChange().length() < 10)
                    .filter(report -> report.getGeneralHealth().length() < 10)
                    .mapToLong(Report::getChatId)
                    .distinct()
                    .toArray();
            for (long num : nums) {
                sendBotMessage(num, text);
            }
        }
    }

    /**
     * Отправляет сообщение пользователю с указанным id.
     * Если отправляющий пользователь не является волонтером, выводит сообщение о том, что писать сообщения
     * пользователю могут только волонтеры и отображает клавиатуру для выбора типа приюта.
     * Если отправляющий пользователь является волонтером, отправляет сообщение пользователю с указанным id.
     *
     * @param update Объект, содержащий информацию о сообщении от пользователя
     */
    public void sendMessageToUser(Update update) {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "Писать сообщения" +
                    " пользователю могут только волонтёры!!!");
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            String messageText = update.getMessage().getText();
            String text = messageText.substring(8, messageText.indexOf(" "));
            int id = Integer.parseInt(text);
            User msg = userService.getUserById(id);
            String message = messageText.substring(messageText.indexOf(" "));
            sendBotMessage(msg.getChatId(), "Cообщение от волонтёра: " + update.getMessage()
                    .getChat().getFirstName() + "\n" + message);
        }
    }

    /**
     * Отправляет сообщение об изъятии животного. Метод доступен только волонтерам.
     * Если пользователь не является волонтером или уведомления выключены, отправляется сообщение с просьбой обратиться к волонтеру.
     * Если пользователь является волонтером, отправляется сообщение о том, что на данный момент не рекомендуется брать нового питомца,
     * и при необходимости взять питомца в будущем, обращение к волонтеру будет радостью.
     *
     * @param update объект, содержащий информацию о сообщении
     * @throws InterruptedException если возникают проблемы при выполнении метода execute класса TelegramApi
     */
    public void sendTakePet(Update update) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "Писать сообщение " +
                    "об изъятии животного" +
                    " могут только волонтёры!!!");
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            String messageText = update.getMessage().getText();
            String text = messageText.substring(9);
            int id = Integer.parseInt(text);
            Report msg = reportService.getReportById(id);
            String message = EmojiParser.parseToUnicode(GRIMACING_FACE + "Уважаемые усыновитель!\n" +
                    "С сожалением сообщаем, что на данный момент будет лучше, если вы останетесь без нового питомца. Понимаем ваше желание поддержать животное, ценим ваши усилия, приложенные для этого, но также видим сложности, с которыми вам приходится сталкиваться и справляться.\n" +
                    "Если у вас появится желание в будущем взять питомца, мы будем рады вам помочь, сейчас, возможно, не то время.\n" +
                    "В ближайшее время с вами свяжутся наши волонтёры, чтобы обсудить способы возвращения животного в приют.");
            sendBotMessage(msg.getChatId(), message);
        }
    }

    /**
     * Отправляет сообщение об уведомлении усыновителя о продлении испытательного срока на 30 дней.
     *
     * @param update Объект типа Update, содержащий информацию о сообщении пользователя в телеграмме
     * @throws InterruptedException Исключение, возникающее при попытке выполнения недопустимой операции с потоком
     */
    public void sendDay30(Update update) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "Писать сообщение " +
                    "о продлении испытательного срока" +
                    " могут только волонтёры!!!");
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            String messageText = update.getMessage().getText();
            String text = messageText.substring(12);
            int id = Integer.parseInt(text);
            Report msg = reportService.getReportById(id);
            String message = EmojiParser.parseToUnicode(CONFUSED_FACE + "Уважаемый усыновитель! Мы рады, что вы взяли животное и заботитесь о нём. Видим, что вы привязались друг к другу и заботитесь о новом питомце.\n" +
                    "Тем не менее у нас есть уверенность, что испытательный срок нужно продлить ещё на 30 дней.\n" +
                    "Надеемся на ваше понимание и желаем скорейшего прохождения второй части испытательного срока!");
            sendBotMessage(msg.getChatId(), message);
        }
    }

    /**
     * Отправляет сообщение об уведомлении усыновителя о продлении испытательного срока на 14 дней.
     *
     * @param update Объект типа Update, содержащий информацию о сообщении пользователя в телеграмме
     * @throws InterruptedException Исключение, возникающее при попытке выполнения недопустимой операции с потоком
     */
    public void sendDay14(Update update) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "Писать сообщение " +
                    "о продлении испытательного срока" +
                    " могут только волонтёры!!!");
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            String messageText = update.getMessage().getText();
            String text = messageText.substring(12);
            int id = Integer.parseInt(text);
            Report msg = reportService.getReportById(id);
            String message = EmojiParser.parseToUnicode(CONFUSED_FACE + "Уважаемый усыновитель! Мы рады, что вы взяли животное и заботитесь о нём. Видим, что вы привязались друг к другу и заботитесь о новом питомце.\n" +
                    "Тем не менее у нас есть уверенность, что испытательный срок нужно продлить ещё на 14 дней.\n" +
                    "Надеемся на ваше понимание и желаем скорейшего прохождения второй части испытательного срока!");
            sendBotMessage(msg.getChatId(), message);
        }
    }

    /**
     * Отправляет сообщение об успешном прохождении испытательного срока.
     *
     * @param update Объект типа Update, содержащий информацию о сообщении пользователя в телеграмме
     * @throws InterruptedException Исключение, возникающее при попытке выполнения недопустимой операции с потоком
     */
    public void sendCongratulation(Update update) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "Писать поздравительное сообщение" +
                    " могут только волонтёры!!!");
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            String messageText = update.getMessage().getText();
            String text = messageText.substring(17);
            int id = Integer.parseInt(text);
            Report msg = reportService.getReportById(id);
            String message = EmojiParser.parseToUnicode(HUNDRED_POINTS + "Дорогой усыновитель!\n" +
                    "От души поздравляем с прохождением испытательного срока! Пусть ваш новый пушистый друг продолжает радовать вас и ваших близких! Успехов!\n" +
                    "Если появится вопросы о животном, Мы на связи!");
            sendBotMessage(msg.getChatId(), message);
        }
    }

    /**
     * Метод отправляет сообщение об ошибочном заполнении отчета усыновителем
     *
     * @param update Обновление, содержащее информацию о сообщении
     * @throws InterruptedException Исключение, возникающее при прерывании потока
     */
    public void sendMessageAboutBadReport(Update update) throws InterruptedException {
        User user1 = userService.findByChatId(update.getMessage().getChatId());
        if (user1 == null || user1.isNotified() == false) {
            sendBotMessage(update.getMessage().getChatId(), "Писать сообщения о плохом" +
                    " отчёте могут только волонтёры!!!");
            try {
                execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            String messageText = update.getMessage().getText();
            String text = messageText.substring(11);
            int id = Integer.parseInt(text);
            Report msg = reportService.getReportById(id);
            String message = "Дорогой усыновитель," + "\n" +
                    "По вашему отчёту " + "\n" +
                    msg.toString() + "\n" +
                    " мы заметили, что ты заполняешь отчет не так подробно," +
                    " как необходимо." + EmojiParser.parseToUnicode(Emoji.CONFUSED_FACE) + " Пожалуйста, подойди ответственнее к этому" +
                    " занятию. В противном случае волонтеры приюта будут" +
                    " обязаны самолично проверять условия содержания животного";
            sendBotMessage(msg.getChatId(), message);
        }
    }

    /**
     * Обработчик действий по умолчанию. Определяет тип действия, которое нужно выполнить,
     * в зависимости от состояния пользователя и сообщения, и запускает соответствующий метод.
     *
     * @param update Обновление сообщения от пользователя
     * @param message Сообщение от пользователя
     * @throws InterruptedException если возникает прерывание потока
     * @throws TelegramApiException если возникает ошибка в API Telegram
     */
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
            if (dog.getStateId() < 6 && dog.getChatId() == update.getMessage().getChatId()) {
                sendDog(update, dog);
            }
        }

        List<Cat> cats = catService.findAllCats();
        for (Cat cat : cats) {
            if (cat.getStateId() < 6 && cat.getChatId() == update.getMessage().getChatId()) {
                sendCat(update, cat);
            }
        }

        List<User> users = userService.getAllUsers();
        List<User> userList = users
                .stream()
                .filter(user1 -> user1.isNotified() == true)
                .collect(Collectors.toList());
        for (User user1 : userList) {
            if (update.getMessage().getChatId() != user1.getChatId() && userService.findByChatId(update.
                    getMessage().getChatId()).isNotified() == true) {
                sendBotMessage(user1.getChatId(), "Волонтёр: " +
                        update.getMessage().getChat().getFirstName() + "\n" + update.getMessage().getText());
            }
        }
        System.out.println(message.getText());
        System.out.println(message.getMessageId());
        log.info(update.getMessage().getChatId() + " " + message.getText());
    }

    /**
     * Отправляет сообщение с вопросом администраторам бота.
     *
     * @param msg Объект сообщения типа CallVolunteerMsg, содержащий текст вопроса.
     */
    public void sendQuestionsToAdmins(CallVolunteerMsg msg) {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.isNotified() == true) {
                sendBotMessage(user.getChatId(), msg.toString());
            }
        }
    }

    /**
     * Метод получения всех отчетов из бота. Если пользователь не является волонтером или не уведомлен, отправляется сообщение о том, что просматривать отчеты может только волонтер. В противном случае вызывается метод getAllReports() и выводятся все отчеты. После этого вызывается метод getTypeOfShelter() для вывода клавиатуры выбора типа приюта.
     *
     * @param update Обновление, полученное от Telegram API.
     * @throws InterruptedException Возникает, когда поток исполнения прерывается.
     */
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
    /**
     * Метод, запускающий бота. Выводит сообщение приветствия с фотографией, отправляет клавиатуру со списком доступных опций.
     *
     * @param update объект Update, содержащий данные о сообщении от пользователя
     * @throws InterruptedException если происходит ошибка во время ожидания выполнения методов библиотеки TelegramBotsApi
     */
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

    /**
     * Метод отправляет информацию о всех собаках в базе данных бота в чат, из которого был произведен запрос.
     * К информации о собаке добавляется фотография.
     *
     * @param update Содержит информацию о запросе пользователя.
     */
    public void getAllDogs(Update update) {

        List<Dog> dogs = dogService.getAllDogs();
        for (Dog dog : dogs) {
            sendBotMessage(update.getCallbackQuery().getMessage().getChatId(),
                    "Собака: " + dog.getId() + "\n" + dog.getNickname() + "\n" +
                            "Кличка: " + dog.getNickname() + "\n" +
                            "Возраст: " + dog.getAge() + "\n" +
                            "Порода: " + dog.getBreed() + "\n" +
                            "Тип: " + dog.getDogType() + "\n" +
                            "Описание: " + dog.getDescription() + "\n");
            sendPhotoFromByteCode(update.getCallbackQuery().getMessage().getChatId(), dog.getDogPhoto());
        }

        try {
            execute(keyboards.getTypeOfShelter(update.getCallbackQuery().getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Метод отправляет информацию о всех котах в базе данных бота в чат, из которого был произведен запрос.
     * К информации о коте добавляется фотография.
     *
     * @param update Содержит информацию о запросе пользователя.
     */
    public void getAllCats(Update update) {
        List<Cat> cats = catService.findAllCats();
        for (Cat cat : cats) {
            sendBotMessage(update.getCallbackQuery().getMessage().getChatId(),
                    "Кошка: " + cat.getId() + "\n" +
                            "Кличка: " + cat.getName() + "\n" +
                            "Возраст: " + cat.getAge() + "\n" +
                            "Порода: " + cat.getBreed() + "\n" +
                            "Тип " + cat.getCatType() + "\n" +
                            "Информация о коте/кошке: \n" + cat.getDescription() + "\n");
            sendPhotoFromByteCode(update.getCallbackQuery().getMessage().getChatId(), cat.getPhoto());
        }
        try {
            execute(keyboards.getTypeOfShelter(update.getCallbackQuery().getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Метод получает список всех отчетов и отправляет их пользователю.
     *
     * @param update Объект, содержащий информацию о сообщении от пользователя
     */
    public void getAllReports(Update update) {
        List<Report> reports = reportService.getAllReports();
        for (Report report : reports) {
            sendBotMessage(update.getMessage().getChatId(), "Отчёт: " + report.getId() + "\n" +
                    "Дата: " + report.getMsgDate() + "\n" +
                    report.getUserInfo() + "\n" +
                    (report.getDog() == null ? "Собака: Нет" : report.getDog().toString()) + "\n" +
                    (report.getCat() == null ? "Кошка: Нет" : report.getCat().toString()) + "\n" +
                    report.getDiet() + "\n" +
                    report.getGeneralHealth() + "\n" +
                    report.getBehaviorChange() + "\n");
            sendPhotoFromByteCode(update.getMessage().getChatId(), report.getPhoto());
        }
    }

    /**
     * Метод отправляет фотоотчёт вместе с информацией о пользователе.
     *
     * @param update объект класса Update, содержащий сообщение пользователя.
     * @param report объект класса Report, содержащий информацию о состоянии животного и прочее.
     * @throws InterruptedException при возникновении ошибки выполнения потока.
     */
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

    /**
     * Метод для обработки сообщения с фотографией, отправленной для добавления собаки в базу данных.
     * Фотография устанавливается для собаки, сохраняется новое состояние, после чего
     * собака сохраняется в базу данных. Пользователю отправляется сообщение о том, что собака добавлена.
     *
     * @param update сообщение с фотографией и данными о пользователе
     * @param dog объект класса Dog, содержащий данные о добавляемой собаке
     * @throws InterruptedException если возникла ошибка при попытке отправки сообщения
     */
    public void sendPhotoForDog(Update update, Dog dog) throws InterruptedException {
        dog.setDogPhoto(getPhotoFromMessage(update));
        sendBotMessage(update.getMessage().getChatId(), "Собака добавлена");
        dog.setStateId(7);
        dogService.saveDog(dog);
        try {
            execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Метод для обработки сообщения с фотографией, отправленной для добавления кота в базу данных.
     * Фотография устанавливается для кота, сохраняется новое состояние, после чего
     * кот сохраняется в базу данных. Пользователю отправляется сообщение о том, что кошка добавлена.
     *
     * @param update сообщение с фотографией и данными о пользователе
     * @param cat объект класса Dog, содержащий данные о добавляемой собаке
     * @throws InterruptedException если возникла ошибка при попытке отправки сообщения
     */
    public void sendPhotoForCat(Update update, Cat cat) throws InterruptedException {
        cat.setPhoto(getPhotoFromMessage(update));
        sendBotMessage(update.getMessage().getChatId(), "Кошка добавлена");
        cat.setStateId(7);
        catService.saveCat(cat);
        try {
            execute(keyboards.getTypeOfShelter(update.getMessage().getChatId()));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Отправляет запрос на создание отчёта пользователю, у которого есть питомец.
     * Создаёт новый отчёт в базе данных, устанавливая начальное состояние, данные пользователя, питомца и текущую дату.
     * Отправляет сообщения с приветствием и вопросом о состоянии питомца, а также клавиатуру
     * для ответа на этот вопрос.
     *
     * @param update Объект, содержащий информацию о событии обновления бота.
     * @throws InterruptedException Исключение, возникающее, когда происходит ошибка при работе с потоками.
     * @throws TelegramApiException Исключение, возникающее при ошибках при работе с Telegram API.
     */
    public void sendReportQuery(Update update) throws InterruptedException, TelegramApiException {
        User user = userService.findByChatId(update.getCallbackQuery().getMessage().getChatId());
        Report report = new Report();
        report.setStateId(1);
        Cat cat = user.getCat();
        if (cat != null) {
            report.setCat(cat);
        }
        Dog dog = user.getDog();
        if (dog != null) {
            report.setDog(dog);
        }
        report.setMsgDate(Timestamp.valueOf(LocalDateTime.now()));
        report.setChatId(Math.toIntExact(update.getCallbackQuery().getMessage().getChatId()));
        reportService.saveReport(report);
        sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Мы рады, что вы забрали у нас питомца)))");
        sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Как поживает наш друг?");
        execute(keyboards.getBackButtonForReport(update.getCallbackQuery().getMessage().getChatId(),
                "Какая диета у питомца?"));
    }

    /**
     * Метод для отправки отчёта на сервер.
     * Метод получает данные от пользователя и в зависимости от состояния добавляет информацию в объект Report.
     * В случае необходимости, отправляет кнопки для быстрого возврата к предыдущему состоянию.
     *
     * @param update объект Update, содержащий информацию о пользователе и его сообщении.
     * @param report объект Report, содержащий информацию об отчёте.
     * @throws InterruptedException в случае прерывания потока.
     * @throws TelegramApiException в случае ошибки при отправке сообщения.
     */
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

    /**
     * Метод запускает процесс обработки запроса на помощь, создавая объект CallVolunteerMsg и записывая его в базу данных.
     * В случае отсутствия зарегистрированного пользователя возвращает сообщение об ошибке.
     * Проходит по состояниям объекта CallVolunteerMsg, вызывая методы ввода и обработки состояний,
     * сохраняя изменения состояния объекта в базу данных.
     * @param update Объект Update, содержащий запрос на помощь.
     */
    private void askQuestion(Update update) {
        final long chatId = update.getCallbackQuery().getMessage().getChatId();
        MessageContext context = null;
        MessageState state;
        User user = userService.findByChatId(chatId);
        if (user == null||user.getStateID()<3) {
            sendBotMessage(chatId, "Задавать вопросы могут только зарегистрированные пользователи" +
                    "\n /registration");
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

    /**
     * Отправляет образец и правила отчетности собаки пользователю в чат, а также запрашивает необходимую информацию по отчету.
     *
     * @param chatId id чата, в который отправляется образец отчета
     * @param messageID id сообщения с образцом отчета
     * @param update объект типа Update, содержащий информацию о запросе пользователя
     * @throws TelegramApiException в случае ошибки при выполнении Telegram API запроса
     * @throws InterruptedException в случае ошибки при выполнении операции с потоком
     */
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

    /**
     * Отправляет образец и правила отчетности кота пользователю в чат, а также запрашивает необходимую информацию по отчету.
     *
     * @param chatId id чата, в который отправляется образец отчета
     * @param messageID id сообщения с образцом отчета
     * @param update объект типа Update, содержащий информацию о запросе пользователя
     * @throws TelegramApiException в случае ошибки при выполнении Telegram API запроса
     * @throws InterruptedException в случае ошибки при выполнении операции с потоком
     */
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
            } else if (dataCallback.equals(BACK_ADD_CAT)) {
                List<Cat> catList = catService.findAllCats();
                List<Cat> cats = catList
                        .stream()
                        .filter(cat -> cat.getChatId() == update.getCallbackQuery().getMessage().getChatId())
                        .filter(cat -> cat.getStateId() < 7)
                        .collect(Collectors.toList());
                catService.deleteCatById(cats.get(cats.size() - 1).getId());
                execute(keyboards.getTypeOfShelter(chatId));
            } else if (dataCallback.equals(BACK_ADD_DOG)) {
                List<Dog> dogList = dogService.getAllDogs();
                List<Dog> dogs = dogList
                        .stream()
                        .filter(dog -> dog.getChatId() == update.getCallbackQuery().getMessage().getChatId())
                        .filter(dog -> dog.getStateId() < 7)
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
                if (user == null||user.getStateID()<3) {
                    sendBotMessage(update.getCallbackQuery().getMessage().getChatId(), "Отправлять отчёт могут только " +
                            "зарегистрированные пользователи!!!\n" +
                            "/registration");
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
            } else if (dataCallback.equals(PUPPY_TYPE)) {
                List<Dog> dogs = dogService.getAllDogs();
                List<Dog> dogList = dogs
                        .stream()
                        .filter(dog -> dog.getStateId() < 7)
                        .collect(Collectors.toList());
                for (Dog dog : dogList) {
                    if (dog.getChatId() == update.getCallbackQuery().getMessage().getChatId())
                        setDogType(PUPPY_TYPE);
                    sendBotMessage(chatId, "Вы выбрали - щенка. \n" +
                            "Для продолжения напишите что-нибудь в чат");
                }
            } else if (dataCallback.equals(KITTEN_TYPE)) {
                List<Cat> cats = catService.findAllCats();
                List<Cat> catList = cats
                        .stream()
                        .filter(cat -> cat.getStateId() < 7)
                        .collect(Collectors.toList());
                for (Cat cat : catList) {
                    if (cat.getChatId() == update.getCallbackQuery().getMessage().getChatId()) {
                        setCatType(KITTEN_TYPE);
                        sendBotMessage(chatId, "Вы выбрали - котёнка. \n" +
                                "Для продолжения напишите что-нибудь в чат");
                    }
                }

            } else if (dataCallback.equals(ADULT_TYPE)) {
                List<Dog> dogs = dogService.getAllDogs();
                List<Dog> dogList = dogs
                        .stream()
                        .filter(dog -> dog.getStateId() < 7)
                        .collect(Collectors.toList());
                for (Dog dog : dogList) {
                    if (dog.getChatId() == update.getCallbackQuery().getMessage().getChatId()) {
                        setDogType(ADULT_TYPE);
                        sendBotMessage(chatId, "Вы выбрали - взрослую собаку. \n" +
                                "Для продолжения напишите что-нибудь в чат");
                    }
                }
            } else if (dataCallback.equals(DISABLED_TYPE_CAT)) {
                List<Cat> cats = catService.findAllCats();
                List<Cat> catList =  cats
                        .stream()
                        .filter(cat -> cat.getStateId() < 7)
                        .collect(Collectors.toList());
                for (Cat cat : catList) {
                    if (cat.getChatId() == update.getCallbackQuery().getMessage().getChatId()) {
                        setCatType(DISABLED_TYPE_CAT);
                        sendBotMessage(chatId, "Вы выбрали - кошку с ограниченными возможностями. \n" +
                                "Для продолжения напишите что-нибудь в чат");
                    }
                }
            } else if (dataCallback.equals(ADULT_TYPE_CAT)) {
                List<Cat> cats = catService.findAllCats();
                List<Cat> catList = cats
                        .stream()
                        .filter(cat -> cat.getStateId() < 7)
                        .collect(Collectors.toList());
                for (Cat cat : catList) {
                    if (cat.getChatId() == update.getCallbackQuery().getMessage().getChatId()) {
                        setCatType(ADULT_TYPE_CAT);
                        sendBotMessage(chatId, "Вы выбрали - взрослую кошку. \n" +
                                "Для продолжения напишите что-нибудь в чат");
                    }
                }
            } else if (dataCallback.equals(DISABLED_TYPE)) {
                List<Dog> dogs = dogService.getAllDogs();
                List<Dog> dogList = dogs
                        .stream()
                        .filter(dog -> dog.getStateId() < 7)
                        .collect(Collectors.toList());
                for (Dog dog : dogList) {
                    if (dog.getChatId() == update.getCallbackQuery().getMessage().getChatId()) {
                        setDogType(DISABLED_TYPE);
                        sendBotMessage(chatId, "Вы выбрали - собаку с ограниченными возможностями. \n" +
                                "Для продолжения напишите что-нибудь в чат");
                    }
                }
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
            } else if (dataCallback.equals(GO_START)) {
                execute(keyboards.getTypeOfShelterEdit(chatId, messageId));
            } else if (dataCallback.equals(GALLERY_DOG)) {
                getAllDogs(update);
            } else if (dataCallback.equals(GALLERY_CAT)) {
                getAllCats(update);
            }
        }
    }

}