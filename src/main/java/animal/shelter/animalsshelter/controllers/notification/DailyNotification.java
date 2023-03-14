package animal.shelter.animalsshelter.controllers.notification;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.ReportService;
import animal.shelter.animalsshelter.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;


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

    @Scheduled(cron = "0 12 * * *", zone = "Europe/Moscow")
    public void dailyReportRemain() {
        for (User user : userService.getAllUsers()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(user.getChatId());
            sendMessage.setText("Не забудь отправить отчет насчет своей собаки!");
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Не удалось отправить сообщение пользователю с chatId {}", user.getChatId(), e);
            }
        }
    }

    @Scheduled(cron = "*/10 * * * *", zone = "Europe/Moscow")
    public void sendReportReminderForDays(){
        for (Report report : reportService.getAllReports()) {
            if (report.getMsgDate().toLocalDateTime().toLocalDate().isBefore(LocalDate.now().minusDays(2))) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId((long) report.getChatId());
                sendMessage.setText("Вы не отправляли отчет о своем питомце уже 2 дня. Пожалуйста, отправьте отчет, \" +\n" +
                        "\"иначе вы можете лишиться своего домашнего животного.\"");
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    logger.error("Не удалось отправить сообщение пользователю с chatId {}", report.getChatId(), e);
                }
            } else if (report.getMsgDate().toLocalDateTime().toLocalDate().isBefore(LocalDate.now().minusDays(3))) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId((long) report.getChatId());
                sendMessage.setText("Вы не отправляли отчет о своем питомце уже 3 дня. Пожалуйста, отправьте отчет, \" +\n" +
                        "\"иначе вы завтра мы будем вынуждены отобрать у вас животное.\"");
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    logger.error("Не удалось отправить сообщение пользователю с chatId {}", report.getChatId(), e);
                }
            }
            // добавить удаление собаки у пользователя (доделать)
        }
    }
}