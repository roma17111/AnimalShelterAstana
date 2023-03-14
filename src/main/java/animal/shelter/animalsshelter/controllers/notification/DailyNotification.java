package animal.shelter.animalsshelter.controllers.notification;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.ReportService;
import animal.shelter.animalsshelter.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;

@Component
public class DailyNotification {

    private final TelegramLongPollingBot bot;
    private final UserService userService;
    private final ReportService reportService;
    private static final Logger logger = LoggerFactory.getLogger(DailyNotification.class);

    public DailyNotification(TelegramLongPollingBot bot, UserService userService, ReportService reportService) {
        this.bot = bot;
        this.userService = userService;
        this.reportService = reportService;
    }

    @Scheduled(cron = "0 0 12 * * *", zone = "Europe/Moscow")
    private void dailyReportRemain() {
        for (User user : userService.getAllUsers()) {
            if (user.getCat() != null || user.getDog() != null) {
                sendNotification(user.getChatId(), "Не забудь отправить отчет насчет своего животного!");
            }
        }
    }

    @Scheduled(cron = "0 0 */3 * * *", zone = "Europe/Moscow")
    private void sendReportReminderForDays(){
        sendReportReminder(AnimalType.DOG);
        sendReportReminder(AnimalType.CAT);
    }

    private void sendReportReminder(AnimalType animalType) {
        for (Report report : reportService.getAllReports()) {
            if (report.getMsgDate().toLocalDateTime().toLocalDate().isBefore(LocalDate.now().minusDays(2))) {
                sendNotification(report.getChatId(), "Вы не отправляли отчет о своем питомце уже 2 дня. Пожалуйста, отправьте отчет, \" +\n" +
                        "\"иначе вы можете лишиться своего домашнего животного.\"");
            } else if (report.getMsgDate().toLocalDateTime().toLocalDate().isBefore(LocalDate.now().minusDays(3))) {
                sendNotification(report.getChatId(), "Вы не отправляли отчет о своем питомце уже 3 дня. Пожалуйста, отправьте отчет, \" +\n" +
                        "\"иначе вы завтра мы будем вынуждены отобрать у вас животное.\"");
            } else if (report.getMsgDate().toLocalDateTime().toLocalDate().isBefore(LocalDate.now().minusDays(4))) {
                sendNotification(report.getChatId(), "Вы не отправляли отчет о своем питомце уже 4 дня!!! \" +\n" +
                        "\"мы вынуждены забрать ваше животное.\"");
                if (animalType == AnimalType.DOG) {
                    userService.deleteDogToUser(userService.findByChatId(report.getChatId()).getId(), report.getDog().getId());
                } else {
                    userService.deleteCatToUser(userService.findByChatId(report.getChatId()).getId(), (int) report.getCat().getId());
                }
            }
        }
    }

    private void sendNotification(long chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            if (e.getMessage().contains("Error executing org.telegram.telegrambots.meta.api.methods.send.SendMessage query: [400] Bad Request: chat not found")) {
                logger.warn("Chat not found while sending notification to chatId {}", chatId);
            } else {
                logger.error("Failed to send notification to chatId {}", chatId, e);
            }
        }
    }

    private enum AnimalType {
        DOG,
        CAT
    }
}