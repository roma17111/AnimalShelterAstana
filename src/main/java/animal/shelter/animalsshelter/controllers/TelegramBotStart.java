package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.repository.testJPA;
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

    public void getMassagePhoto(Update update){
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            PhotoSize photo = getLargestPhoto(message.getPhoto());
            byte[] byteCode = getImageByteCode(photo);
            sendPhoto(chatId, byteCode);
        }
    }

    private PhotoSize getLargestPhoto(List<PhotoSize> photos) {
        return photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
    }

    private byte[] getImageByteCode(PhotoSize photo) {
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(photo.getFileId());
        try {
            File file = execute(getFileMethod);
            String filePath = file.getFilePath();
            URL url = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + filePath);
            InputStream inputStream = url.openConnection().getInputStream();
            return IOUtils.toByteArray(inputStream);
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendPhoto(Long chatId, byte[] byteCode) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(chatId));
        sendPhotoRequest.setPhoto(new InputFile(new ByteArrayInputStream(byteCode), "photo.jpg"));
        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
