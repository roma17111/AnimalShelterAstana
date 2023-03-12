package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.constants.BotServiceVolunteerConstants;
import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import animal.shelter.animalsshelter.model.Dog;
import animal.shelter.animalsshelter.model.User;
import animal.shelter.animalsshelter.model.Volunteer;
import animal.shelter.animalsshelter.repository.CallVolunteerMsgRepository;
import animal.shelter.animalsshelter.service.impl.CallVolunteerMsgServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.annotation.Reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static animal.shelter.animalsshelter.constants.BotServiceDogConstants.*;
import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static animal.shelter.animalsshelter.constants.BotServiceVolunteerConstants.MSG_DATA;
import static animal.shelter.animalsshelter.constants.BotServiceVolunteerConstants.MSG_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CallVolonteerMsgServiceTest {


    @Mock
    private CallVolunteerMsgRepository callVolunteerMsgRepository ;

    @InjectMocks
    private CallVolunteerMsgServiceImpl callVolunteerMsgService;

    @Test
    void saveCallVolunteerMsgTest() {

        Volunteer volunteer = new Volunteer();

        CallVolunteerMsg expected = new CallVolunteerMsg();

        expected.setChatID(CHAT_ID_DEFAULT);
        expected.setId(ID_DEFAULT);
        expected.setStateId(STATE_ID_DEFAULT);
        expected.setNumberPhone(PHONE_DEFAULT);
        expected.setEmail(EMAIL_DEFAULT);
        expected.setMsgDate(MSG_DATA);
        expected.setMsgText(MSG_TEXT);

        when(callVolunteerMsgRepository.save(any(CallVolunteerMsg.class))).thenReturn(expected);
        CallVolunteerMsg actual = callVolunteerMsgService.saveCallVolunteerMsg(expected);

        assertThat(actual.getChatID()).isEqualTo(expected.getChatID());
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStateId()).isEqualTo(expected.getStateId());
        assertThat(actual.getNumberPhone()).isEqualTo(expected.getNumberPhone());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getMsgDate()).isEqualTo(expected.getMsgDate());
        assertThat(actual.getMsgText()).isEqualTo(expected.getMsgText());
    }


    @Test
    void getCallVolunteerMsgServiceByIdTest() {

        Volunteer volunteer = new Volunteer();

        CallVolunteerMsg expected = new CallVolunteerMsg();

        expected.setChatID(CHAT_ID_DEFAULT);
        expected.setId(ID_DEFAULT);
        expected.setStateId(STATE_ID_DEFAULT);
        expected.setNumberPhone(PHONE_DEFAULT);
        expected.setEmail(EMAIL_DEFAULT);
        expected.setMsgDate(MSG_DATA);
        expected.setMsgText(MSG_TEXT);


        when(callVolunteerMsgRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        CallVolunteerMsg actual = callVolunteerMsgService.getCallVolunteerMsgById(BotServiceVolunteerConstants.ID_DEFAULT);

        assertThat(actual.getChatID()).isEqualTo(expected.getChatID());
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStateId()).isEqualTo(expected.getStateId());
        assertThat(actual.getNumberPhone()).isEqualTo(expected.getNumberPhone());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getMsgDate()).isEqualTo(expected.getMsgDate());
        assertThat(actual.getMsgText()).isEqualTo(expected.getMsgText());
    }


    @Test
    void getAllCallVolunteerMsgServiceTest() {

        Volunteer volunteer = new Volunteer();


        ArrayList<CallVolunteerMsg> callVolunteerMsg = new ArrayList<>();

        CallVolunteerMsg callVolunteerMsg1 = new CallVolunteerMsg();
        callVolunteerMsg1.setChatID(CHAT_ID_DEFAULT);
        callVolunteerMsg1.setId(ID_DEFAULT);
        callVolunteerMsg1.setStateId(STATE_ID_DEFAULT);
        callVolunteerMsg1.setNumberPhone(PHONE_DEFAULT);
        callVolunteerMsg1.setEmail(EMAIL_DEFAULT);
        callVolunteerMsg1.setMsgDate(MSG_DATA);
        callVolunteerMsg1.setMsgText(MSG_TEXT);


        CallVolunteerMsg callVolunteerMsg2 = new CallVolunteerMsg();

        callVolunteerMsg2.setChatID(CHAT_ID_DEFAULT);
        callVolunteerMsg2.setId(ID_DEFAULT);
        callVolunteerMsg2.setStateId(STATE_ID_DEFAULT);
        callVolunteerMsg2.setNumberPhone(PHONE_DEFAULT);
        callVolunteerMsg2.setEmail(EMAIL_DEFAULT);
        callVolunteerMsg2.setMsgDate(MSG_DATA);
        callVolunteerMsg2.setMsgText(MSG_TEXT);


        when(callVolunteerMsgRepository.findAll()).thenReturn(callVolunteerMsg);
        Collection<CallVolunteerMsg> actual = callVolunteerMsgService.getAllCallVolunteerMsgs();

        assertThat(actual.size()).isEqualTo(callVolunteerMsg.size());
        assertThat(actual).isEqualTo(callVolunteerMsg);
    }


    @Test
    void deleteCallVolunteerServiceByIdTest() {
        CallVolunteerMsg callVolunteerMsg = new CallVolunteerMsg();

        lenient().when(callVolunteerMsgRepository.findById(callVolunteerMsg.getId())).thenReturn(Optional.of(callVolunteerMsg));
        callVolunteerMsgService.deleteCallVolunteerMsg(callVolunteerMsg.getId());
        verify(callVolunteerMsgRepository).deleteById(callVolunteerMsg.getId());
    }
}

