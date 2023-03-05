package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.repository.testJPA;
import animal.shelter.animalsshelter.service.ImageParser;
import animal.shelter.animalsshelter.service.impl.ImageParserImpl;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

@Log4j
@Component
public class TelegramBotStart extends TelegramLongPollingBot {

    @Autowired
    private testJPA repository;

    private final ImageParser imageParser = new ImageParserImpl(this);

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

    }

    private void getPhoto(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasPhoto()) {
                Message message = update.getMessage();
                Long chatId = message.getChatId();
                PhotoSize photo = imageParser.getLargestPhoto(message.getPhoto());
                byte[] byteCode = imageParser.imageToByteCode(photo);
                // sendPhoto(update.getMessage().getChatId(), byteCode);
                // Отправка данных будет производиться на сервер
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPhoto(Long chatId, byte[] byteCode){
        try {
            execute(imageParser.byteCodeToImage(chatId, byteCode));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
