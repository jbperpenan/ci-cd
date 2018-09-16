package com.jekbp.cicd;

import com.jekbp.cicd.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;
    private HttpHeaders headers;
    private HttpEntity entity;
    private ResponseEntity response;

    @Before
    public void setup () {
        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();
        entity = new HttpEntity<Transaction>(null, headers);

        invokeClearAPI();
    }

    @Test
    public void getStatisticsWithEmptyData() {
        invokeStatisticsAPI();

        String expected = "{\"sum\":0.0,\"avg\":0.0,\"max\":0.0,\"min\":0.0,\"count\":0}";
        assertEquals(expected, response.getBody());
    }

    @Test
    public void getStatisticsWithDataAllOlderThan60Seconds() {
        entity = new HttpEntity<>(createTransaction(2,Instant.ofEpochMilli(1478192204000L).getEpochSecond()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(4,Instant.ofEpochMilli(1530429376000L).getEpochSecond()), headers);
        invokeCreateTransactionAPI();

        invokeStatisticsAPI();

        String expected = "{\"sum\":0.0,\"avg\":0.0,\"max\":0.0,\"min\":0.0,\"count\":0}";
        assertEquals(expected, response.getBody());
    }

    @Test
    public void getStatisticsWithNoOlderThan60Seconds() {
        entity = new HttpEntity<>(createTransaction(6,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(4,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(10,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        invokeStatisticsAPI();

        String expected = "{\"sum\":20.0,\"avg\":6.666666666666667,\"max\":10.0,\"min\":4.0,\"count\":3}";
        assertEquals(expected, response.getBody());
    }

    @Test
    public void getStatisticsWithOneOlderThan60Seconds() {
        entity = new HttpEntity<>(createTransaction(6,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(10,Instant.ofEpochMilli(1478192204000L).getEpochSecond()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(4,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        invokeStatisticsAPI();

        String expected = "{\"sum\":10.0,\"avg\":5.0,\"max\":6.0,\"min\":4.0,\"count\":2}";
        assertEquals(expected, response.getBody());
    }

    @Test
    public void getStatisticsAlternatingData() {
        entity = new HttpEntity<>(createTransaction(1,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(2,Instant.ofEpochMilli(1478192204000L).getEpochSecond()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(3,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(4,Instant.ofEpochMilli(1530429376000L).getEpochSecond()), headers);
        invokeCreateTransactionAPI();

        invokeStatisticsAPI();

        String expected = "{\"sum\":4.0,\"avg\":2.0,\"max\":3.0,\"min\":1.0,\"count\":2}";
        assertEquals(expected, response.getBody());
    }

    @Test
    public void ge() {
        entity = new HttpEntity<>(createTransaction(40000,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(40000,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();

        entity = new HttpEntity<>(createTransaction(40000,Instant.now().toEpochMilli()), headers);
        invokeCreateTransactionAPI();



        invokeStatisticsAPI();

        String expected = "{\"sum\":120000.0,\"avg\":40000.0,\"max\":40000.0,\"min\":40000.0,\"count\":3}";
        assertEquals(expected, response.getBody());
    }

    private Transaction createTransaction (double amount, long timestamp) {
        return new Transaction(amount,timestamp);
    }

    private void invokeCreateTransactionAPI() {
        response = restTemplate.exchange (
                "http://localhost:" + port + "/transactions",
                HttpMethod.POST, entity, String.class);
    }

    private void invokeStatisticsAPI() {
        response = restTemplate.exchange (
                "http://localhost:" + port +"/statistics",
                HttpMethod.GET, entity, String.class);
    }

    private void invokeClearAPI() {
        response = restTemplate.exchange (
                "http://localhost:" + port +"/clear",
                HttpMethod.GET, entity, String.class);
    }
}
