package ru.tikh1y.tacos.repositories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tikh1y.tacos.RowMappers.UserRowMapper;
import ru.tikh1y.tacos.models.User;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@Slf4j
public class JdbcUserRepository implements UserRepository{

    private final JdbcTemplate jdbc;

    public JdbcUserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        List<User> users = this.jdbc.query(query, new UserRowMapper(), username);

        if(users.isEmpty())
        {
            log.info("Пользователь: " + username + " не найден." );
            return null;
        }
        if(users.size() > 1)
        {
            log.error("Пользователя: " + username + " найдено более, чем 1" );
            return null;
        }

        else return users.get(0);
    }

    @Override
    @Transactional
    public User save(User user) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "INSERT INTO users (username, password, fullname, street, city, state, zip, phone) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR);

        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getStreet(),
                user.getCity(),
                user.getState(),
                user.getZip(),
                user.getPhone())
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);

        Map<String, Object> keys = Objects.requireNonNull(keyHolder.getKeys());
        long userId = ((Number) keys.get("id")).longValue();  // 'id' - это имя столбца с первичным ключом
        user.setId(userId);

        return user;
    }
}
