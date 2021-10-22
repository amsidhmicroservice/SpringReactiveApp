package com.amsidh.mvc;

import com.amsidh.mvc.entity.Person;
import com.amsidh.mvc.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class SpringReactiveApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactiveApp.class, args);
    }

    @Bean
    CommandLineRunner loadPersonData(PersonRepository personRepository) {
        /*return args ->
                personRepository
                        .deleteAll()
                        .subscribe(null, null, () -> Flux.interval(Duration.ofSeconds(1))
                                .take(11)
                                .map(i -> i.intValue() + 1)
                                .map(i -> {
                                    Person person = new Person(i, "Amsidh" + i, "Pune" + i, 4121L + i, 81085518L + i, "amsidhlokhande@gmail.com" + i);
                                    personRepository.save(person);
                                    return person;
                                }).subscribe(p -> log.info(String.valueOf(p))));
*/

        return args -> {
            personRepository
                    .deleteAll()
                    .subscribe(null, null, () -> {
                        Flux.interval(Duration.ofSeconds(1))
                                .take(11)
                                .map(i -> i.intValue() + 1)
                                .map(i -> {
                                    return new Person(i, "Amsidh" + i, "Pune" + i, 4121L + i, 81085518L + i, "amsidhlokhande@gmail.com" + i);
                                })
                                .map(record -> personRepository.save(record)
                                        .subscribe(System.out::println))
                                .subscribe();
                    });
        };
    }
}
