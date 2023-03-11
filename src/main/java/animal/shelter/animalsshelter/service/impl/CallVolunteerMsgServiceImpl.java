package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.service.CallVolunteerMsgService;
import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import animal.shelter.animalsshelter.repository.CallVolunteerMsgRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CallVolunteerMsgServiceImpl implements CallVolunteerMsgService {

    private CallVolunteerMsgRepository repository;


    /**
     * Метод для сохранения сообщения для добровольцев в базу данных.
     * @param msg - объект сообщения.
     * @return объект сообщения, сохраненный в базу данных.
     */
    @Override
    public CallVolunteerMsg saveCallVolunteerMsg(CallVolunteerMsg msg) {
        return repository.save(msg);
    }

    /**
     * Метод для получения объекта сообщения для добровольцев из базы данных по id.
     * @param id - id объекта сообщения.
     * @return объект сообщения, найденный по id.
     */
    @Override
    public CallVolunteerMsg getCallVolunteerMsgById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Метод для получения списка всех сообщений для добровольцев из базы данных.
     * @return список всех сообщений для добровольцев в базе данных.
     */
    @Override
    public List<CallVolunteerMsg> getAllCallVolunteerMsgs() {
        return repository.findAll();
    }

    /**
     * Метод для удаления объекта сообщения для добровольцев из базы данных по id.
     * @param id - id объекта сообщения для удаления.
     */
    @Override
    public void deleteCallVolunteerMsg(Long id) {
        repository.deleteById(id);
    }
}