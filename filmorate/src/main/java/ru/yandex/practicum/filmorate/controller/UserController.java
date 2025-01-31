package ru.yandex.practicum.filmorate.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Create user {}", user.getEmail());
        System.out.println("DEBUG1");
        System.out.println(this.userService != null);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Update user {}", user.getEmail());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {

        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

//    @PostConstruct
//    public void init() {
//        System.out.println("✅ UserController создан, userService = " + (userService != null ? "OK" : "NULL"));
//        System.out.println(this != null);
//    }

}
