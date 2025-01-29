package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    //TODO Создание пользователя
    @PostMapping
    private User createUser(@RequestBody User user) {
        return new User();
    }

    //TODO Обновление пользователя
    @PutMapping
    private User updateUser(@RequestBody User user) {
        return new User();
    }

    //TODO Получение списка всех пользователей
    @GetMapping
    private List<User> getAllUsers() {
        return new ArrayList<>(); //empty
    }

}
