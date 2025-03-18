-- Вставка данных в таблицу mpas
INSERT INTO mpas (id, name)
SELECT 1, 'PG-13'
WHERE NOT EXISTS (SELECT 1 FROM mpas WHERE id = 1);

INSERT INTO mpas (id, name)
SELECT 2, 'R'
WHERE NOT EXISTS (SELECT 1 FROM mpas WHERE id = 2);

-- Вставка данных в таблицу genres
INSERT INTO genres (id, name)
SELECT 1, 'Комедия'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE id = 1);

INSERT INTO genres (id, name)
SELECT 2, 'Драма'
WHERE NOT EXISTS (SELECT 1 FROM genres WHERE id = 2);

-- Вставка данных в таблицу directors
INSERT INTO directors (id, name)
SELECT 1, 'Кристофер Нолан'
WHERE NOT EXISTS (SELECT 1 FROM directors WHERE id = 1);

INSERT INTO directors (id, name)
SELECT 2, 'Квентин Тарантино'
WHERE NOT EXISTS (SELECT 1 FROM directors WHERE id = 2);

