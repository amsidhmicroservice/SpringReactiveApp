package com.amsidh.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.Date;

public class SpringWebClientApp {
    public static void main(String[] args) throws Exception {
        new SpringWebClientApp().run(args);
    }


    public void run(String... args) throws Exception {
        //Retrieve All Person http://localhost:8282/persons
        webClientGetCallForAllPerson("http://localhost:8282/persons");

        //Retrieve Person by Person ID http://localhost:8282/persons/{personId}
        //webClientGetCallForPersonByPersonID("http://localhost:8282/persons/{personId}", 1);

        //Retrieve All events associated for a person
        //webClientGetCallForEventsPerPerson("http://localhost:8282/persons/{personId}/events",1);

        System.out.println("End Of Program");
    }

    public void webClientGetCallForAllPerson(String url) {
        getWebClient().get()
                .uri(url)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Person.class))
                .filter(person -> person != null)
                .retryWhen(HttpRetryClass.getWhenFactory(url))
                .toStream()
                .forEach(person -> {
                    System.out.println(person);
                });
    }

    public void webClientGetCallForPersonByPersonID(String url, Integer personId) {
        URI uri = new DefaultUriBuilderFactory(url).builder().build(personId);
        System.out.println(uri.toString());
        getWebClient().get()
                .uri(uri)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Person.class))
                .filter(person -> person != null)
                .retryWhen(HttpRetryClass.getWhenFactory(uri.toString()))
                .toStream()
                .forEach(person -> {
                    System.out.println(person);
                });
    }

    public void webClientGetCallForEventsPerPerson(String url, Integer personId) {
        URI uri = new DefaultUriBuilderFactory(url).builder().build(personId);
        System.out.println(uri.toString());
        getWebClient().get()
                .uri(uri)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(PersonEvent.class))
                .filter(person -> person != null)
                .retryWhen(HttpRetryClass.getWhenFactory(uri.toString()))
                .toStream()
                .forEach(personEvent -> {
                    System.out.println(personEvent);
                });
    }

    public WebClient getWebClient() {
        return WebClient.builder().build();
    }
}

class HttpRetryClass {
    public static Retry<Object> getWhenFactory(String url) {
        return Retry.onlyIf(context -> context.exception() instanceof Exception).doOnRetry(objectRetryContext -> {
            long iteration = objectRetryContext.iteration();
            if (iteration == 5) {
                throw new RuntimeException("Unable to get response from url " + url + " even after trying 5 times and exception is " + objectRetryContext.exception().getMessage());
            } else {
                System.out.println("Retry the http call with current retry count " + iteration + " url is " + url);
            }
        }).exponentialBackoff(Duration.ofSeconds(3), Duration.ofSeconds(5)).retryMax(5);
    }
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