package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.services.StartMenu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotStart extends TelegramLongPollingBot {

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
        long id = update.getMessage().getChatId();
        if (update.hasMessage()&& message.hasText()) {
            sendBotMessage(id,"Вы ввели - " + message.getText());
            System.out.println(message.getText());
        }
    }

    private void sendBotMessage(long id,String name) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(id));
        sendMessage.setText(name);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}
