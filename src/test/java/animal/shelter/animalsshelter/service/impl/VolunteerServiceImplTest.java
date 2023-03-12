package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Volunteer;
import animal.shelter.animalsshelter.repository.VolunteerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static animal.shelter.animalsshelter.constants.BotServicePersonConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VolunteerServiceImplTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @InjectMocks
    private VolunteerServiceImpl volunteerService;

    @Test
    void saveVolunteerTest() {

        Volunteer expected = new Volunteer();

        expected.setId(ID_DEFAULT);
        expected.setChatId(CHAT_ID_DEFAULT);
        expected.setGivenName(NAME_DEFAULT);
        expected.setPhoneNumber(PHONE_DEFAULT);
        expected.setEmail(EMAIL_DEFAULT);

        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(expected);
        Volunteer actual = volunteerService.saveVolunteer(expected);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        assertThat(actual.getGivenName()).isEqualTo(expected.getGivenName());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }


    @Test
    void getDogByIdTest() {

        Volunteer expected = new Volunteer();

        expected.setId(ID_DEFAULT);
        expected.setChatId(CHAT_ID_DEFAULT);
        expected.setGivenName(NAME_DEFAULT);
        expected.setPhoneNumber(PHONE_DEFAULT);
        expected.setEmail(EMAIL_DEFAULT);

        when(volunteerRepository.findById(any(Integer.class))).thenReturn(Optional.of(expected));
        Volunteer actual = volunteerService.getVolunteerById(1);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        assertThat(actual.getGivenName()).isEqualTo(expected.getGivenName());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }


    @Test
    void getAllDogsTest() {

        ArrayList<Volunteer> expected = new ArrayList<>();

        Volunteer volunteer = new Volunteer();
        volunteer.setId(ID_DEFAULT);
        volunteer.setChatId(CHAT_ID_DEFAULT);
        volunteer.setGivenName(NAME_DEFAULT);
        volunteer.setPhoneNumber(PHONE_DEFAULT);
        volunteer.setEmail(EMAIL_DEFAULT);

        Volunteer volunteer1 = new Volunteer();
        volunteer1.setId(ID_DEFAULT);
        volunteer1.setChatId(CHAT_ID_DEFAULT);
        volunteer1.setGivenName(NAME_DEFAULT);
        volunteer1.setPhoneNumber(PHONE_DEFAULT);
        volunteer1.setEmail(EMAIL_DEFAULT);

        Volunteer volunteer2 = new Volunteer();
        volunteer2.setId(ID_DEFAULT);
        volunteer2.setChatId(CHAT_ID_DEFAULT);
        volunteer2.setGivenName(NAME_DEFAULT);
        volunteer2.setPhoneNumber(PHONE_DEFAULT);
        volunteer2.setEmail(EMAIL_DEFAULT);

        when(volunteerRepository.findAll()).thenReturn(expected);
        Collection<Volunteer> actual = volunteerService.getAllVolunteers();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void deleteDogTest() {
        Volunteer volunteer = new Volunteer();

        lenient().when(volunteerRepository.findById(volunteer.getId())).thenReturn(Optional.of(volunteer));
        volunteerService.deleteVolunteer(volunteer.getId());
        verify(volunteerRepository).deleteById(volunteer.getId());
    }
}
