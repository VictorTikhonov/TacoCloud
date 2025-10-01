package ru.tikh1y.tacos.RowMappers;

import org.springframework.jdbc.core.RowMapper;
import ru.tikh1y.tacos.models.Ingredient;
import ru.tikh1y.tacos.models.Taco;
import ru.tikh1y.tacos.repositories.IngredientRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;



public class TacoRowMapper implements RowMapper<Taco> {

//    private final IngredientRepository ingredientRepository;
//
//    public TacoRowMapper(IngredientRepository ingredientRepository) {
//        this.ingredientRepository = ingredientRepository;
//    }

    @Override
    public Taco mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Taco(
                rs.getLong("id"),
                rs.getTimestamp("created_at"),
                rs.getString("name")
        );
    }
}
