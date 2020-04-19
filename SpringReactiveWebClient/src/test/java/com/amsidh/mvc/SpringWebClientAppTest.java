package com.amsidh.mvc;


import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

public class SpringWebClientAppTest {
    private WireMockServer wireMockServer;
    private SpringWebClientApp springWebClientApp;

    @Before
    public void setUp() throws Exception {
        this.wireMockServer = new WireMockServer();
        wireMockServer.start();
        this.springWebClientApp = new SpringWebClientApp();
    }

    @After
    public void tearDown() throws Exception {
        wireMockServer.stop();
    }

    @Test(expected = RuntimeException.class)
    public void webClientGetCallForAllPersonServiceUnAvailable() throws IOException {
        stubFor(get(urlPathMatching("/persons")).willReturn(
                aResponse().withStatus(503)
                        .withBody("!!! Service Unavailable !!!")));
        springWebClientApp.webClientGetCallForAllPerson("http://localhost:8080/persons");
    }
    @Test
    public void webClientGetCallForAllPersonWithStatusOk() throws IOException {
        stubFor(get(urlPathMatching("/persons")).willReturn(
                aResponse().withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("[{\"_id\":1,\"name\":\"Amsidh1\",\"city\":\"Pune1\",\"pinCode\":412105,\"mobileNumber\":8108551841,\"emailId\":\"amsidhlokhande1@gmail.com\"},{\"_id\":8,\"name\":\"Amsidh8\",\"city\":\"Pune8\",\"pinCode\":412103,\"mobileNumber\":8108551848,\"emailId\":\"amsidhlokhande8@gmail.com\"},{\"_id\":3,\"name\":\"Amsidh3\",\"city\":\"Pune3\",\"pinCode\":412107,\"mobileNumber\":8108551843,\"emailId\":\"amsidhlokhande3@gmail.com\"},{\"_id\":7,\"name\":\"Amsidh7\",\"city\":\"Pune7\",\"pinCode\":412102,\"mobileNumber\":8108551847,\"emailId\":\"amsidhlokhande7@gmail.com\"},{\"_id\":6,\"name\":\"Amsidh6\",\"city\":\"Pune6\",\"pinCode\":412101,\"mobileNumber\":8108551846,\"emailId\":\"amsidhlokhande6@gmail.com\"},{\"_id\":9,\"name\":\"Amsidh9\",\"city\":\"Pune9\",\"pinCode\":412104,\"mobileNumber\":8108551849,\"emailId\":\"amsidhlokhande9@gmail.com\"},{\"_id\":5,\"name\":\"Amsidh5\",\"city\":\"Pune5\",\"pinCode\":412109,\"mobileNumber\":8108551845,\"emailId\":\"amsidhlokhande5@gmail.com\"},{\"_id\":4,\"name\":\"Amsidh4\",\"city\":\"Pune4\",\"pinCode\":412108,\"mobileNumber\":8108551844,\"emailId\":\"amsidhlokhande4@gmail.com\"},{\"_id\":2,\"name\":\"Amsidh2\",\"city\":\"Pune2\",\"pinCode\":412106,\"mobileNumber\":8108551842,\"emailId\":\"amsidhlokhande2@gmail.com\"},{\"_id\":10,\"name\":\"Amsidh10\",\"city\":\"Pune10\",\"pinCode\":412105,\"mobileNumber\":8108551810,\"emailId\":\"amsidhlokhande10@gmail.com\"}]")));
        springWebClientApp.webClientGetCallForAllPerson("http://localhost:8080/persons");
    }
    @Test
    public void webClientGetCallForPersonByPersonID() {
    }

    @Test
    public void webClientGetCallForEventsPerPerson() {
    }

    private String convertHttpResponseToString(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        return convertInputStreamToString(inputStream);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }
}