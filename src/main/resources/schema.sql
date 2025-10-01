-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,                     -- Уникальный идентификатор пользователя
                                     username VARCHAR(255) NOT NULL,            -- Имя пользователя (логин)
                                     password VARCHAR(255) NOT NULL,            -- Пароль пользователя
                                     fullname VARCHAR(255) NOT NULL,            -- Полное имя пользователя
                                     street VARCHAR(255) NOT NULL,              -- Улица проживания пользователя
                                     city VARCHAR(100) NOT NULL,                -- Город проживания пользователя
                                     state VARCHAR(100) NOT NULL,               -- Штат/область проживания пользователя
                                     zip VARCHAR(20) NOT NULL,                  -- Почтовый индекс пользователя
                                     phone VARCHAR(20) NOT NULL                 -- Номер телефона пользователя
);


-- Создание таблицы заказов
CREATE TABLE IF NOT EXISTS Taco_Order (
                                          id SERIAL PRIMARY KEY,                     -- Уникальный идентификатор заказа
                                          delivery_name VARCHAR(50) NOT NULL,        -- Имя для доставки
                                          delivery_street VARCHAR(50) NOT NULL,      -- Улица для доставки
                                          delivery_city VARCHAR(50) NOT NULL,        -- Город для доставки
                                          delivery_state VARCHAR(50) NOT NULL,        -- Штат/область для доставки
                                          delivery_zip VARCHAR(10) NOT NULL,         -- Почтовый индекс для доставки
                                          cc_number VARCHAR(16) NOT NULL,            -- Номер кредитной карты
                                          cc_expiration VARCHAR(5) NOT NULL,         -- Срок действия кредитной карты
                                          cc_cvv VARCHAR(3) NOT NULL,                -- CVV код кредитной карты
                                          place_at TIMESTAMP NOT NULL,               -- Дата и время размещения заказа
                                          user_id BIGINT NOT NULL,                   -- Связь с таблицей пользователей
                                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Внешний ключ на таблицу пользователей
);

-- Создание таблицы Taco
CREATE TABLE IF NOT EXISTS Taco (
                                    id SERIAL PRIMARY KEY,                     -- Уникальный идентификатор тако
                                    name VARCHAR(50) NOT NULL,                 -- Название тако
                                    taco_order BIGINT NOT NULL,                -- Внешний ключ на таблицу заказов
                                    taco_order_key BIGINT NOT NULL,            -- Ключ тако в контексте заказа
                                    created_at TIMESTAMP NOT NULL,             -- Дата и время создания тако
                                    FOREIGN KEY (taco_order) REFERENCES Taco_Order(id) ON DELETE CASCADE -- Связь с таблицей заказов
);

-- Создание таблицы ингредиентов
CREATE TABLE IF NOT EXISTS Ingredient (
                                          id VARCHAR(4) PRIMARY KEY,                 -- Уникальный идентификатор ингредиента
                                          name VARCHAR(25) NOT NULL,                 -- Название ингредиента
                                          type VARCHAR(10) NOT NULL                  -- Тип ингредиента
);

-- Создание таблицы для связи ингредиентов с тако
CREATE TABLE IF NOT EXISTS Ingredient_Ref (
                                              ingredient VARCHAR(4) NOT NULL,            -- Внешний ключ на таблицу ингредиентов
                                              taco BIGINT NOT NULL,                      -- Внешний ключ на таблицу Taco
                                              taco_key BIGINT NOT NULL,                  -- Ключ тако в контексте заказа
                                              PRIMARY KEY (ingredient, taco),            -- Составной первичный ключ
                                              FOREIGN KEY (ingredient) REFERENCES Ingredient(id) ON DELETE CASCADE, -- Связь с таблицей ингредиентов
                                              FOREIGN KEY (taco) REFERENCES Taco(id) ON DELETE CASCADE -- Связь с таблицей тако
);






-- DROP TABLE IF EXISTS Ingredient_Ref CASCADE;
-- DROP TABLE IF EXISTS Ingredient CASCADE;
-- DROP TABLE IF EXISTS Taco CASCADE;
-- DROP TABLE IF EXISTS Taco_Order CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;


