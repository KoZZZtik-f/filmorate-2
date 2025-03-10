package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Received request to create user with email: {}", user.getEmail());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Received request to update user with email: {}", user.getEmail());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        log.info("Received request to get user by ID: {}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Received request to get all users");
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.info("Received request to add friend with ID: {} to user with ID: {}", friendId, id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.info("Received request to remove friend with ID: {} from user with ID: {}", friendId, id);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllUserFriends(@PathVariable("id") Integer id) {
        log.info("Received request to get all friends for user with ID: {}", id);
        return userService.getUserAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        log.info("Received request to get common friends for user with ID: {} and user with ID: {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }



//    @PostConstruct
//    public void init() {
//        System.out.println("✅ UserController создан, userService = " + (userService != null ? "OK" : "NULL"));
//        System.out.println(this != null);
//    }

}
