package ru.tikh1y.tacos.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tikh1y.tacos.RowMappers.IngredientRowMapper;
import ru.tikh1y.tacos.models.Ingredient;


import java.util.List;
import java.util.Optional;



@Repository
public class JdbcIngredientRepository implements IngredientRepository {
    private final JdbcTemplate jdbc;

    public JdbcIngredientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Ingredient> findAll() {
        String query = "SELECT id, name, type FROM Ingredient";
        return jdbc.query(query, new IngredientRowMapper());
    }

    @Override
    public Optional<Ingredient> findById(String id) {
        String query = "SELECT id, name, type FROM Ingredient WHERE id = ?";
        List<Ingredient> ingredients = jdbc.query(query, new IngredientRowMapper(), id);
        return ingredients.isEmpty() ? Optional.empty() : Optional.of(ingredients.get(0));
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        String query = "INSERT INTO Ingredient (id, name, type) VALUES (?, ?, ?)";
        jdbc.update(query, ingredient.getId(), ingredient.getName(), ingredient.getType().toString());
        return ingredient;
    }
}
