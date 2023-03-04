package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.repository.testJPA;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Component
public class TelegramBotStart extends TelegramLongPollingBot {

    @Autowired
    private testJPA repository;

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.key}")
    private String botKey;
    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botKey;
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        Long chatId = update.getMessage().getChatId();

        long id = update.getMessage().getChatId();
        if (update.hasMessage()&& message.hasText()) {
            var ywop = repository.findById(1L);
            sendBotMessage(id,"Вы ввели - " + message.getText());
            sendBotMessage(id,"Вы ввели - " + ywop.toString());
            //sendBotMessage(id,"Id - " + chatId);
            System.out.println(message.getText());
            log.info(id+" "+message.getText());
        }
    }

    private void sendBotMessage(long id,String name) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(id));
        sendMessage.setText(name);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
