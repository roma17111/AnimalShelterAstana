package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.constants.BotServicePersonConstants;
import animal.shelter.animalsshelter.model.CallVolunteerMsg;
import animal.shelter.animalsshelter.repository.CallVolunteerMsgRepository;
import animal.shelter.animalsshelter.service.CallVolunteerMsgService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static animal.shelter.animalsshelter.constants.BotServiceDogConstants.*;
import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CallVolunteerTest {

    @Mock
    private CallVolunteerMsgRepository callVolunteerMsgRepository;
    @InjectMocks
    private CallVolunteerMsgServiceImpl callVolunteerMsgService;

    @Test
    void saveMsgTest() {

        CallVolunteerMsg expected = new CallVolunteerMsg();

        expected.setStateId(STATE_ID_DEFAULT2);
        expected.setNumberPhone(String.valueOf(12345));
        expected.setEmail(EMAIL_CORRECT);
        expected.setMsgText(PHONE_DEFAULT);
     ;

        when(callVolunteerMsgRepository.save(any(CallVolunteerMsg.class))).thenReturn(expected);
        CallVolunteerMsg actual = callVolunteerMsgService.saveCallVolunteerMsg(expected);

        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getMsgText()).isEqualTo(expected.getMsgText());
        assertThat(actual.getStateId()).isEqualTo(expected.getStateId());
        assertThat(actual.getNumberPhone()).isEqualTo(expected.getNumberPhone());
    }


    @Test
    void getMsgByIdTest() {

        CallVolunteerMsg expected = new CallVolunteerMsg();
        expected.setId(ID_DEFAULT);
        expected.setStateId(STATE_ID_DEFAULT2);
        expected.setNumberPhone(String.valueOf(12345));
        expected.setEmail(EMAIL_CORRECT);
        expected.setMsgText(PHONE_DEFAULT);;

        when(callVolunteerMsgRepository.findById(any(Long.class))).thenReturn(Optional.of(expected));
        CallVolunteerMsg actual = callVolunteerMsgService.getCallVolunteerMsgById(Long.valueOf(ID_DEFAULT));

        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getMsgText()).isEqualTo(expected.getMsgText());
        assertThat(actual.getStateId()).isEqualTo(expected.getStateId());
        assertThat(actual.getNumberPhone()).isEqualTo(expected.getNumberPhone());
    }


    @Test
    void getAllMsgsTest() {

        CallVolunteerMsg msg = new CallVolunteerMsg();

        ArrayList<CallVolunteerMsg> expected = new ArrayList<>();

        CallVolunteerMsg msg1 = new CallVolunteerMsg();
        msg1.setId(ID_DEFAULT);
        msg1.setStateId(STATE_ID_DEFAULT2);
        msg1.setNumberPhone(String.valueOf(12345));
        msg1.setEmail(EMAIL_CORRECT);
        msg1.setMsgText(PHONE_DEFAULT);
        expected.add(msg);
        expected.add(msg1);
        when(callVolunteerMsgRepository.findAll()).thenReturn(expected);
        Collection<CallVolunteerMsg> actual = callVolunteerMsgService.getAllCallVolunteerMsgs();
        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void deleteMsgTest() {
        CallVolunteerMsg msg = new CallVolunteerMsg();

        lenient().when(callVolunteerMsgRepository.findById(msg.getId())).thenReturn(Optional.of(msg));
        callVolunteerMsgService.deleteCallVolunteerMsg(msg.getId());
        verify(callVolunteerMsgRepository).deleteById(msg.getId());
    }
}
