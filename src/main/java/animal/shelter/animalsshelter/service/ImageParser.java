package animal.shelter.animalsshelter.service;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ImageParser {

    /**
     * Конвертирует фото в байт-код.
     *
     * @param photo объект с фото, которое нужно конвертировать
     * @return массив байтов с изображением
     * @throws TelegramApiException если что-то пошло не так при выполнении запроса к Telegram API
     * @throws IOException          если что-то пошло не так при чтении данных с удаленного сервера
     */
    byte[] imageToByteCode(PhotoSize photo) throws TelegramApiException, IOException;

    /**
     * Отправляет фото с заданным байт-кодом.
     *
     * @param chatId    идентификатор чата, в который нужно отправить фото
     * @param byteCode  массив байтов с изображением
     * @return объект с запросом на отправку фото
     */
    SendPhoto byteCodeToImage(Long chatId, byte[] byteCode);

    /**
     * Возвращает наибольший объект PhotoSize из списка.
     *
     * @param photos список объектов PhotoSize для поиска наибольшего
     * @return наибольший объект PhotoSize из списка или null, если список пуст
     */
    PhotoSize getLargestPhoto(List<PhotoSize> photos);

}
