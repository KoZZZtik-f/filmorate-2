-- 1. Независимые таблицы без внешних ключей
CREATE TABLE IF NOT EXISTS genres (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS mpas (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS directors (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(30) NOT NULL
);

-- 2. Основные сущности
CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(30) NOT NULL,
                                     login VARCHAR(50) NOT NULL,
                                     name VARCHAR(50),
                                     birthday DATE
);

CREATE TABLE IF NOT EXISTS films (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(30),
                                     description VARCHAR(200),
                                     release_date DATE,
                                     duration INT,
                                     director_id INT,
                                     mpa_id INT,
                                     CONSTRAINT films_mpa_fk FOREIGN KEY (mpa_id) REFERENCES mpas(id),
                                     CONSTRAINT films_director_fk FOREIGN KEY (director_id) REFERENCES directors(id)
);

-- 3. Связующие таблицы
CREATE TABLE IF NOT EXISTS films_genres (
                                            film_id INT NOT NULL,
                                            genre_id INT NOT NULL,
                                            CONSTRAINT films_genres_film_fk FOREIGN KEY (film_id) REFERENCES films(id),
                                            CONSTRAINT films_genres_genre_fk FOREIGN KEY (genre_id) REFERENCES genres(id),
                                            PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
                                     film_id INT NOT NULL,
                                     user_id INT NOT NULL,
                                     PRIMARY KEY (film_id, user_id),
                                     CONSTRAINT likes_film_fk FOREIGN KEY (film_id) REFERENCES films(id),
                                     CONSTRAINT likes_user_fk FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS friendships (
                                           user_id INT NOT NULL,
                                           friend_id INT NOT NULL,
                                           PRIMARY KEY (user_id, friend_id),
                                           CONSTRAINT friendships_user_fk FOREIGN KEY (user_id) REFERENCES users(id),
                                           CONSTRAINT friendships_friend_fk FOREIGN KEY (friend_id) REFERENCES users(id)
);

-- 4. Отзывы (последними, так как зависят от films и users)
CREATE TABLE IF NOT EXISTS reviews (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       content VARCHAR(300) NOT NULL,
                                       film_id INT NOT NULL,
                                       user_id INT NOT NULL,
                                       is_positive BOOLEAN NOT NULL,
                                       created_at TIMESTAMP NOT NULL,
                                       CONSTRAINT reviews_filmId_fk FOREIGN KEY (film_id) REFERENCES films(id),
                                       CONSTRAINT reviews_userId_fk FOREIGN KEY (user_id) REFERENCES users(id)
);