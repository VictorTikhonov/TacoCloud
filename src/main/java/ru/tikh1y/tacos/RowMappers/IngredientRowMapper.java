package ru.tikh1y.tacos.RowMappers;

import org.springframework.jdbc.core.RowMapper;
import ru.tikh1y.tacos.models.Ingredient;
import ru.tikh1y.tacos.models.Type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientRowMapper implements RowMapper<Ingredient> {

    @Override
    public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Ingredient(
                rs.getString("id"),
                rs.getString("name"),
                Type.valueOf(rs.getString("type")));
    }
}
