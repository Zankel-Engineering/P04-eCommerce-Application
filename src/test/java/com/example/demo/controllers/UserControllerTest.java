package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.demo.controllers.TestUtils.PASSWORD;
import static com.example.demo.controllers.TestUtils.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final CartRepository cartRepo = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private final String THIS_IS_HASHED = "thisIsHashed";

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
        when(encoder.encode(PASSWORD)).thenReturn(THIS_IS_HASHED);
    }

    @Test
    public void createUser() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(USERNAME);
        r.setPassword(PASSWORD);
        r.setConfirmPassword(PASSWORD);
        final ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(USERNAME, u.getUsername());
        assertEquals(THIS_IS_HASHED, u.getPassword());
    }

    @Test
    public void findById() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(USERNAME);
        r.setPassword(PASSWORD);
        r.setConfirmPassword(PASSWORD);
        final ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.ofNullable(user));

        final ResponseEntity<User> userResponseEntity = userController.findById(0L);

        User u = userResponseEntity.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(USERNAME, u.getUsername());
        assertEquals(THIS_IS_HASHED, u.getPassword());
    }

    @Test
    public void findByUserName() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(USERNAME);
        r.setPassword(PASSWORD);
        r.setConfirmPassword(PASSWORD);
        final ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();
        when(userRepo.findByUsername(USERNAME)).thenReturn(user);

        final ResponseEntity<User> userResponseEntity = userController.findByUserName(USERNAME);

        User u = userResponseEntity.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(USERNAME, u.getUsername());
        assertEquals(THIS_IS_HASHED, u.getPassword());
    }
}