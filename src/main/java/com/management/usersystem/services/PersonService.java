package com.management.usersystem.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.management.usersystem.dto.PersonDTO;
import com.management.usersystem.models.Person;
import com.management.usersystem.repositories.PersonRepository;
import com.management.usersystem.util.PersonNotFoundException;
import com.management.usersystem.util.enums.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public PersonService(PersonRepository personRepository, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public PersonDTO findById(int id) {
        Optional<Person> person = personRepository.findById(id);
        return convertToPersonDTO(person.orElseThrow(PersonNotFoundException::new));
    }

    public List<PersonDTO> findAll() {
        return personRepository.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public void save(PersonDTO personDTO) {
        Person person = convertToPerson(personDTO);
        enrichPerson(person);
        personRepository.save(person);
    }
    @Transactional
    public void update(int id, JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        PersonDTO existingPerson = findById(id);
        PersonDTO personDTOToSave = applyPatchToPerson(jsonPatch, existingPerson);
        Person personToSave = convertToPerson(personDTOToSave);
        personToSave.setUpdatedAt(LocalDateTime.now());
        personRepository.save(personToSave);
    }

    private PersonDTO applyPatchToPerson(JsonPatch jsonPatch,  PersonDTO personDTO) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(personDTO, JsonNode.class));
        return objectMapper.treeToValue(patched, PersonDTO.class);
    }

    @Transactional
    public void deleteById(int id) {
        if (!personRepository.existsById(id)) {
            throw new PersonNotFoundException();
        }
        personRepository.deleteById(id);
    }
    public List<PersonDTO> filterByFirstName() {
        return personRepository.filterByFirstName().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    public List<PersonDTO> filterByLastName() {
        return personRepository.filterByLastName().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    public List<PersonDTO> filterByCreatedAt() {
        return personRepository.filterByCreatedAt().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
    public PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    private static void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setRole(Role.ROLE_USER);
    }
}
