-- Заполнение таблицы genres
INSERT INTO genres (id, name) VALUES (1, 'Комедия');
INSERT INTO genres (id, name) VALUES (2, 'Драма');
INSERT INTO genres (id, name) VALUES (3, 'Мультфильм');
INSERT INTO genres (id, name) VALUES (4, 'Триллер');
INSERT INTO genres (id, name) VALUES (5, 'Документальный');
INSERT INTO genres (id, name) VALUES (6, 'Боевик');

-- Заполнение таблицы mpas
INSERT INTO mpas (id, name) VALUES (1, 'G');
INSERT INTO mpas (id, name) VALUES (2, 'PG');
INSERT INTO mpas (id, name) VALUES (3, 'PG-13');
INSERT INTO mpas (id, name) VALUES (4, 'R');
INSERT INTO mpas (id, name) VALUES (5, 'NC-17');

-- Заполнение таблицы directors
INSERT INTO directors (id, name) VALUES (1, 'Кристофер Нолан');
INSERT INTO directors (id, name) VALUES (2, 'Квентин Тарантино');
INSERT INTO directors (id, name) VALUES (3, 'Стивен Спилберг');

-- Заполнение таблицы users
INSERT INTO users (id, email, login, name, birthday) VALUES (1, 'user1@example.com', 'user1', 'Иван Иванов', '1990-01-01');
INSERT INTO users (id, email, login, name, birthday) VALUES (2, 'user2@example.com', 'user2', 'Петр Петров', '1995-05-15');
INSERT INTO users (id, email, login, name, birthday) VALUES (3, 'user3@example.com', 'user3', 'Алексей Алексеев', '1985-12-31');

-- Заполнение таблицы films
INSERT INTO films (id, name, description, release_date, duration, director_id, mpa_id) VALUES (1, 'Фильм 1', 'Описание фильма 1', '2020-01-01', 120, 1, 3);
INSERT INTO films (id, name, description, release_date, duration, director_id, mpa_id) VALUES (2, 'Фильм 2', 'Описание фильма 2', '2021-05-15', 90, 2, 4);
INSERT INTO films (id, name, description, release_date, duration, director_id, mpa_id) VALUES (3, 'Фильм 3', 'Описание фильма 3', '2022-12-31', 150, 3, 2);

-- Заполнение таблицы films_genres
INSERT INTO films_genres (film_id, genre_id) VALUES (1, 1);
INSERT INTO films_genres (film_id, genre_id) VALUES (1, 2);
INSERT INTO films_genres (film_id, genre_id) VALUES (2, 3);
INSERT INTO films_genres (film_id, genre_id) VALUES (3, 4);
INSERT INTO films_genres (film_id, genre_id) VALUES (3, 6);

-- Заполнение таблицы likes
INSERT INTO likes (film_id, user_id) VALUES (1, 1);
INSERT INTO likes (film_id, user_id) VALUES (1, 2);
INSERT INTO likes (film_id, user_id) VALUES (2, 3);
INSERT INTO likes (film_id, user_id) VALUES (3, 1);

-- Заполнение таблицы friendships
INSERT INTO friendships (user_id, friend_id) VALUES (1, 2);
INSERT INTO friendships (user_id, friend_id) VALUES (2, 3);

-- Заполнение таблицы reviews
INSERT INTO reviews (id, content, film_id, user_id, is_positive, created_at) VALUES (1, 'Отличный фильм!', 1, 1, true, '2023-01-01 12:00:00');
INSERT INTO reviews (id, content, film_id, user_id, is_positive, created_at) VALUES (2, 'Не очень понравилось', 2, 2, false, '2023-02-15 14:30:00');
INSERT INTO reviews (id, content, film_id, user_id, is_positive, created_at) VALUES (3, 'Интересный сюжет', 3, 3, true, '2023-03-20 18:45:00');