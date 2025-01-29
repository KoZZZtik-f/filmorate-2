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
    private void createUser(@RequestBody User user) {

    }

    //TODO Обновление пользователя

    //TODO Получение списка всех пользователей
    @GetMapping
    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>() ;
        users.add(new User());
        return users;
    }

}
