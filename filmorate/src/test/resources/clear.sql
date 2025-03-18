-- Отключение проверки внешних ключей
SET REFERENTIAL_INTEGRITY FALSE;

-- Удаление данных из зависимых таблиц
DELETE FROM likes;
DELETE FROM friendships;
DELETE FROM reviews;
DELETE FROM films_genres;

-- Удаление данных из основных таблиц
DELETE FROM films;
DELETE FROM users;
DELETE FROM directors;
DELETE FROM genres;
DELETE FROM mpas;

-- Сброс идентификаторов
ALTER TABLE reviews ALTER COLUMN id RESTART WITH 1;
ALTER TABLE films ALTER COLUMN id RESTART WITH 1;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE directors ALTER COLUMN id RESTART WITH 1;
ALTER TABLE genres ALTER COLUMN id RESTART WITH 1;
ALTER TABLE mpas ALTER COLUMN id RESTART WITH 1;

-- Включение проверки внешних ключей
SET REFERENTIAL_INTEGRITY TRUE;