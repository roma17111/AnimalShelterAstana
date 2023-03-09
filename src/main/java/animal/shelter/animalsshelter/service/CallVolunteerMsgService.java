package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import animal.shelter.animalsshelter.model.User;

import java.util.List;

public interface CallVolunteerMsgService {

    /**
     * Метод для сохранения сообщения для добровольцев в базу данных.
     * @param msg - объект сообщения.
     * @return объект сообщения, сохраненный в базу данных.
     */
    CallVolunteerMsg saveCallVolunteerMsg(CallVolunteerMsg msg);

    /**
     * Метод для получения объекта сообщения для добровольцев из базы данных по id.
     * @param id - id объекта сообщения.
     * @return объект сообщения, найденный по id.
     */
    CallVolunteerMsg getCallVolunteerMsgById(Long id);

    /**
     * Метод для получения списка всех сообщений для добровольцев из базы данных.
     * @return список всех сообщений для добровольцев в базе данных.
     */
    List<CallVolunteerMsg> getAllCallVolunteerMsgs();

    /**
     * Метод для удаления объекта сообщения для добровольцев из базы данных по id.
     * @param id - id объекта сообщения для удаления.
     */
    void deleteCallVolunteerMsg(Long id);

}