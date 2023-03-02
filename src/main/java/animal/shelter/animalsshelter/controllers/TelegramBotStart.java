package animal.shelter.animalsshelter.controllers;

import animal.shelter.animalsshelter.model.TestEntity;
import animal.shelter.animalsshelter.util.HibernateUtil;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

        addTestEntity();

        long id = update.getMessage().getChatId();
        if (update.hasMessage()&& message.hasText()) {
            sendBotMessage(id,"Вы ввели - " + message.getText());
            System.out.println(message.getText());
            log.info(id+" "+message.getText());
        }
    }

    public void addTestEntity() {
        TestEntity emp = new TestEntity();
        emp.setName("David");
        emp.setAge(15);

        //Get Session
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        //start transaction
        session.beginTransaction();
        //Save the Model object
        session.save(emp);
        //Commit transaction
        session.getTransaction().commit();

        System.out.println("Test = ="+emp.getId());

        //terminate session factory, otherwise program won't end
        sessionFactory.close();
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
