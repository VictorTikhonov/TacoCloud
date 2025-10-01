package ru.tikh1y.tacos.repositories;

import ru.tikh1y.tacos.models.User;

public interface UserRepository {
    User findByUsername(String username);

    User save(User user);
}
