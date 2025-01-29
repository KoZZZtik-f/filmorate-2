package ru.yandex.practicum.filmorate.validator;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ReleaseDate {

    String message() default "Указана неверная дата.";

    Class<?>[] groups() default {};


    boolean optional() default false;
}