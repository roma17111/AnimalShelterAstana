package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.service.ImageParser;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

public class ImageParserImpl implements ImageParser {

    private final TelegramLongPollingBot telegramLongPollingBot;

    public ImageParserImpl(TelegramLongPollingBot options)   {
        this.telegramLongPollingBot = options;
    }

    /**
     * Конвертирует фото в байт-код.
     *
     * @param photo объект с фото, которое нужно конвертировать
     * @return массив байтов с изображением
     * @throws TelegramApiException если что-то пошло не так при выполнении запроса к Telegram API
     * @throws IOException          если что-то пошло не так при чтении данных с удаленного сервера
     */
    public byte[] imageToByteCode(PhotoSize photo) throws TelegramApiException, IOException {
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(photo.getFileId());
        try {
            File file = telegramLongPollingBot.execute(getFileMethod);
            String filePath = file.getFilePath();
            URL url = new URL("https://api.telegram.org/file/bot" + telegramLongPollingBot.getBotToken() + "/" + filePath);
            try (InputStream inputStream = url.openConnection().getInputStream()) {
                return IOUtils.toByteArray(inputStream);
            }
        } catch (TelegramApiException e) {
            throw new TelegramApiException("Failed to execute 'getFile' method", e);
        } catch (IOException e) {
            throw new IOException("Failed to read data from remote server", e);
        }
    }


    /**
     * Отправляет фото с заданным байт-кодом.
     *
     * @param chatId    идентификатор чата, в который нужно отправить фото
     * @param byteCode  массив байтов с изображением
     * @return объект с запросом на отправку фото
     */
    public SendPhoto byteCodeToImage(Long chatId, byte[] byteCode) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(chatId));
        sendPhotoRequest.setPhoto(new InputFile(new ByteArrayInputStream(byteCode), "photo.jpg"));
        return sendPhotoRequest;
    }

    /**
     * Возвращает наибольший объект PhotoSize из списка.
     *
     * @param photos список объектов PhotoSize для поиска наибольшего
     * @return наибольший объект PhotoSize из списка или null, если список пуст
     */
    public PhotoSize getLargestPhoto(List<PhotoSize> photos) {
        return photos == null || photos.isEmpty()
                ? null
                : photos.stream()
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null);
    }
}
