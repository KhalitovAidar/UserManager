package com.management.usersystem.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.management.usersystem.dto.PersonDTO;
import com.management.usersystem.services.PersonService;
import com.management.usersystem.util.PersonErrorResponse;
import com.management.usersystem.util.PersonNotCreatedException;
import com.management.usersystem.util.PersonNotFoundException;
import com.management.usersystem.util.enums.FilterType;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;

    @Autowired
    public PeopleController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<PersonDTO> getPeople() {
        return personService.findAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public PersonDTO getById(@PathVariable("id") int id) throws PersonNotFoundException {
        return personService.findById(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            StringBuilder message = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError: fieldErrors) {
                message.append(fieldError.getField())
                        .append(" - ")
                        .append(fieldError.getDefaultMessage())
                        .append(";");
            }

            throw new PersonNotCreatedException(message.toString());
        }

        personService.save(personDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/filter_by/{filter}")
    public List<PersonDTO> getPeopleFilterBy(@PathVariable("filter") FilterType filter) {
        switch (filter){
            case firstname -> {
                return personService.filterByFirstName();
            }
            case lastname -> {
                return personService.filterByLastName();
            }
            case created_at -> {
                return personService.filterByCreatedAt();
            }
        }
        return Collections.emptyList();
    }


    @PatchMapping(path = "update/{id}")
    public ResponseEntity<HttpStatus> updatePerson(@PathVariable("id") int id,
                                                   @RequestBody JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        personService.update(id, jsonPatch);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") int id) {
        personService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<PersonErrorResponse> handler(PersonNotFoundException e) {
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                "Person with this id wasn't found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonNotCreatedException.class)
    public ResponseEntity<PersonErrorResponse> handler(PersonNotCreatedException e) {
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JsonPatchException.class, HttpMessageNotReadableException.class,
                       ConstraintViolationException.class})
    public ResponseEntity<PersonErrorResponse> handler() {
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                "JSON невалиден",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }
}