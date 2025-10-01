package ru.tikh1y.tacos.RowMappers;

import org.springframework.jdbc.core.RowMapper;
import ru.tikh1y.tacos.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Извлекаем данные из ResultSet и передаем их в конструктор User
        return new User(
                rs.getLong("id"),            // id пользователя
                rs.getString("username"),     // username
                rs.getString("password"),     // password
                rs.getString("fullname"),     // fullname
                rs.getString("street"),       // street
                rs.getString("city"),         // city
                rs.getString("state"),        // state
                rs.getString("zip"),          // zip
                rs.getString("phone")         // phoneNumber
        );
    }
}


