package com.amsidh.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackageClasses = PersonMongoRepository.class)
public class SpringReactiveApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringReactiveApp.class, args);
    }

    @Bean
    CommandLineRunner getCommandLineRunner(PersonMongoRepository personMongoRepository) {
        return (args) -> {
            personMongoRepository.deleteAll().subscribe(null, null, () -> {
                Stream.of(
                        new Person(1, "Amsidh1", "Pune1", 412105L, 8108551841L, "amsidhlokhande1@gmail.com"),
                        new Person(2, "Amsidh2", "Pune2", 412106L, 8108551842L, "amsidhlokhande2@gmail.com"),
                        new Person(3, "Amsidh3", "Pune3", 412107L, 8108551843L, "amsidhlokhande3@gmail.com"),
                        new Person(4, "Amsidh4", "Pune4", 412108L, 8108551844L, "amsidhlokhande4@gmail.com"),
                        new Person(5, "Amsidh5", "Pune5", 412109L, 8108551845L, "amsidhlokhande5@gmail.com"),
                        new Person(6, "Amsidh6", "Pune6", 412101L, 8108551846L, "amsidhlokhande6@gmail.com"),
                        new Person(7, "Amsidh7", "Pune7", 412102L, 8108551847L, "amsidhlokhande7@gmail.com"),
                        new Person(8, "Amsidh8", "Pune8", 412103L, 8108551848L, "amsidhlokhande8@gmail.com"),
                        new Person(9, "Amsidh9", "Pune9", 412104L, 8108551849L, "amsidhlokhande9@gmail.com"),
                        new Person(10, "Amsidh10", "Pune10", 412105L, 8108551810L, "amsidhlokhande10@gmail.com")
                ).forEach(person -> personMongoRepository.save(person).subscribe(System.out::println));
            });
        };


    }

    @Bean
    RouterFunction getRouteFunction(PersonMongoRepository personMongoRepository) {
        return RouterFunctions.route(RequestPredicates.GET("/persons"),
                request -> {
                    return ServerResponse.ok().body(personMongoRepository.findAll(), Person.class);
                }).andRoute(RequestPredicates.GET("/persons/{personId}"), request -> {
            return ServerResponse.ok().body(personMongoRepository.findById(Integer.parseInt(request.pathVariable("personId"))), Person.class);
        }).andRoute(RequestPredicates.GET("/persons/{personId}/events"), request -> {
            return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(
                    personMongoRepository.findById(Integer.parseInt(request.pathVariable("personId"))).flatMapMany(person -> {
                        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(2));
                        Flux<PersonEvent> personEventFlux = Flux.fromStream(Stream.generate(() -> {
                            return new PersonEvent(person, new Date());
                        }));
                        return Flux.zip(intervalFlux, personEventFlux).map(tuple2 -> tuple2.getT2());
                    }), PersonEvent.class);
        }).andRoute(RequestPredicates.POST("/persons"), request -> {
            Mono<Person> personMono = request.bodyToMono(Person.class);
            personMongoRepository.save(request.bodyToMono(Person.class).block());
            return ServerResponse.ok().body(personMongoRepository.findAll(), Person.class);
        });
    }

}

interface PersonMongoRepository extends ReactiveMongoRepository<Person, Integer> {

}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {
    private Integer _id;
    private String name;
    private String city;
    private Long pinCode;
    private Long mobileNumber;
    private String emailId;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class PersonEvent {
    private Person person;
    private Date date;
}

