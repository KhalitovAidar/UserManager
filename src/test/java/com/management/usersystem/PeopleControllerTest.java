package com.management.usersystem;

import com.management.usersystem.controllers.PeopleController;
import com.management.usersystem.dto.PersonDTO;
import com.management.usersystem.services.PersonService;
import com.management.usersystem.util.PersonNotCreatedException;
import com.management.usersystem.util.PersonNotFoundException;
import com.management.usersystem.util.enums.FilterType;
import org.hibernate.collection.spi.PersistentBag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeopleControllerTest {
    @Mock
    private PersonService personService;

    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private PeopleController peopleController;

    @Test
    public void testGetPeople() {
        // Ожидаемый список PersonDTO
        List<PersonDTO> expectedPeople = List.of(
                new PersonDTO("John","Doe",
                                 "john@example.com", "password"),
                new PersonDTO("Jane", "Smith",
                                "jane@example.com", "password")
        );

        // Настройка поведения mock-объекта
        when(personService.findAll()).thenReturn(expectedPeople);

        // Вызов метода getPeople()
        List<PersonDTO> actualPeople = peopleController.getPeople();

        // Проверка результата
        assertEquals(expectedPeople, actualPeople);
    }

    @Test
    public void testGetByIdReturnsValidPerson() {
        PersonDTO expectedPerson = new PersonDTO("John", "Wick",
                                            "john@gmail.com", "password");

        when(personService.findById(1)).thenReturn(expectedPerson);

        PersonDTO actualPerson = peopleController.getById(1);

        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void testGetByIdThrowsPersonNotFoundException() {
        when(personService.findById(-1)).thenThrow(PersonNotFoundException.class);
        assertThrows(PersonNotFoundException.class, () -> peopleController.getById(-1));
    }

    @Test
    public void testCreateValidPersonReturnsOkStatus() {
        PersonDTO personDTO = new PersonDTO("John", "Wick",
                "john@gmail.com", "password");

        ResponseEntity<HttpStatus> expectedValue = ResponseEntity.ok(HttpStatus.OK);

        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<HttpStatus> actualValue = peopleController.create(personDTO, bindingResult);

        assertEquals(expectedValue, actualValue);
        verify(personService, times(1)).save(personDTO);
    }

    @Test
    public void testCreateInvalidPersonReturnsBadRequest() {
        PersonDTO invalidPersonDTO = new PersonDTO("", "",
                "something", "password");

        FieldError fieldErrorFirstname = new FieldError(
                "invalidPersonDTO",
                "firstname",
                "Field firstname can't be empty"
        );
        FieldError fieldErrorLastname = new FieldError(
                "invalidPersonDTO",
                "lastname",
                "Field lastname can't be empty"
        );
        FieldError fieldErrorEmail = new FieldError(
                "invalidPersonDTO",
                "email",
                "Field lastname can't be empty"
        );

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldErrorFirstname);
        fieldErrors.add(fieldErrorLastname);
        fieldErrors.add(fieldErrorEmail);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        assertThrows(PersonNotCreatedException.class, () -> peopleController.create(invalidPersonDTO, bindingResult));
    }

    @Test
    public void testGetPeopleFilterByFirstname() {
        //TODO
        List<PersonDTO> expectedPeopleList = Arrays.asList(
                new PersonDTO(
                        "John", "Wick",
                        "john@gmail.com", "password"
                ),
                new PersonDTO(
                        "Aidar","Zaga",
                        "khalitov@gmail.com", "password"
                )
        );

        when(personService.filterByFirstName()).thenReturn(expectedPeopleList);

        List<PersonDTO> actualPeopleList = peopleController.getPeopleFilterBy(FilterType.firstname);

        assertEquals(expectedPeopleList, actualPeopleList);
    }

    @Test
    public void testGetPeopleFilterByLastname() {
        //TODO
        List<PersonDTO> expectedPeopleList = Arrays.asList(
                new PersonDTO(
                        "John", "Wick",
                        "john@gmail.com", "password"
                ),
                new PersonDTO(
                        "Aidar","Zaga",
                        "khalitov@gmail.com", "password"
                )
        );

        when(personService.filterByLastName()).thenReturn(expectedPeopleList);

        List<PersonDTO> actualPeopleList = peopleController.getPeopleFilterBy(FilterType.lastname);

        assertEquals(expectedPeopleList, actualPeopleList);
    }

    @Test
    public void testGetPeopleFilterByCreatedAt() {
        //TODO
        List<PersonDTO> expectedPeopleList = Arrays.asList(
                new PersonDTO(
                        "John", "Wick",
                        "john@gmail.com", "password"
                ),
                new PersonDTO(
                        "Aidar","Zaga",
                        "khalitov@gmail.com", "password"
                )
        );

        when(personService.filterByCreatedAt()).thenReturn(expectedPeopleList);

        List<PersonDTO> actualPeopleList = peopleController.getPeopleFilterBy(FilterType.created_at);

        assertEquals(expectedPeopleList, actualPeopleList);
    }

    @Test
    public void testUpdatePerson() {

    }

    @Test
    public void testDeletePerson() {

    }
}
