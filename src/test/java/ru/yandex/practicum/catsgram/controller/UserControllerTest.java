package ru.yandex.practicum.catsgram.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.catsgram.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private UserController userController;
    User user;
    private TestRestTemplate restTemplate;
    private String url;

    @BeforeEach
    void beforeEach() {
        user = new User();
        restTemplate = new TestRestTemplate();
        url = "http://localhost:" + port + "/users";
    }

    @Test
    public void getTest() {
        user.setEmail("mail@yandex.ru");
        user.setUsername("User");
        user.setPassword("123");
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        System.out.println(response.getBody().getId());
        User user2 = new User();
        user2.setEmail("mail2@yandex.ru");
        user2.setUsername("User2");
        user2.setPassword("321");
        ResponseEntity<User> response2 = restTemplate.postForEntity(url, user2, User.class);
        System.out.println(response2.getBody().getId());

    }



}
