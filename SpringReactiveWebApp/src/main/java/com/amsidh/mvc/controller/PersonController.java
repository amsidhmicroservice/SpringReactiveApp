package com.amsidh.mvc.controller;

import com.amsidh.mvc.controller.response.PersonEvent;
import com.amsidh.mvc.entity.Person;
import com.amsidh.mvc.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/persons")
@CrossOrigin(origins = "*")
public class PersonController {

    private final PersonRepository personRepository;

    @GetMapping
    public Flux<Person> getPersons() {
        log.info("Fetching all persons using api /persons");
        return personRepository.findAll();
    }

    @GetMapping(value = "/stream1")
    public Flux<ServerSentEvent<Person>> getPersonsStream1() {
        log.info("Fetching all persons using api /persons/stream1");
        return personRepository.findAll().delayElements(Duration.ofSeconds(1)).
                map(person -> ServerSentEvent.<Person>builder()
                        .comment("Fetching person with Id:" + person.get_id())
                        .id(person.get_id().toString())
                        .event("periodic-event")
                        .data(person)
                        .build());
    }

    @GetMapping(value = "/stream2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Person> getPersonsStream2() {
        log.info("Fetching all persons using api /persons/stream2");
        return personRepository.findAll().delayElements(Duration.ofSeconds(1));
    }


    @GetMapping(value = "/stream3", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Person> getPersonsStream3() {
        log.info("Fetching all persons using api /persons/stream2");
        return personRepository.findAll().delayElements(Duration.ofSeconds(1));
    }


    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PersonEvent> getPersonEvents() {
        log.info("Fetching person events in reactive way using api /persons/{personId}/events");
        return personRepository.findAll().map(person -> PersonEvent.builder().person(person).date(new Date()).build()).delayElements(Duration.ofSeconds(1));
    }

}
