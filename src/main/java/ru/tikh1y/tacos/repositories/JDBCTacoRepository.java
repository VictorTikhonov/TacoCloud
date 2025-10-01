package ru.tikh1y.tacos.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tikh1y.tacos.RowMappers.TacoRowMapper;
import ru.tikh1y.tacos.models.Ingredient;
import ru.tikh1y.tacos.models.Taco;
import ru.tikh1y.tacos.models.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JDBCTacoRepository implements TacoRepository {
    private final JdbcTemplate jdbc;

    public JDBCTacoRepository(JdbcTemplate jdbc, IngredientRepository ingredientRepository) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public List<Taco> findAll() {
        String query = "SELECT t.id AS taco_id, t.name AS taco_name, t.created_at, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.type AS ingredient_type " +
                "FROM Taco t " +
                "INNER JOIN Ingredient_Ref ir ON t.id = ir.taco " +
                "INNER JOIN Ingredient i ON ir.ingredient = i.id " +
                "ORDER BY t.created_at DESC " +
                "LIMIT 15";

        Map<Long, Taco> tacoMap = new HashMap<>();

        jdbc.query(query, rs -> {
            Long tacoId = rs.getLong("taco_id");
            Taco taco = tacoMap.computeIfAbsent(tacoId, id -> {
                Taco t = new Taco();
                try {
                    t.setId(id);
                    t.setName(rs.getString("taco_name"));
                    t.setCreatedAt(rs.getTimestamp("created_at"));
                } catch (SQLException e) {
                    throw new RuntimeException("Error setting taco fields", e);
                }
                return t;
            });

            Ingredient ingredient = new Ingredient(
                    rs.getString("ingredient_id"),
                    rs.getString("ingredient_name"),
                    Type.valueOf(rs.getString("ingredient_type"))
            );

            taco.setIngredient(ingredient);
        });

        return new ArrayList<>(tacoMap.values());
    }


    //    @Override
//    @Transactional
//    public List<Taco> findAll() {
//        String query = "SELECT * FROM taco ORDER BY created_at DESC LIMIT 15";
//        List<Taco> tacos = jdbc.query(query, new TacoRowMapper(ingredientRepository));
//
//        for (Taco taco : tacos) {
//            List<String> ingredientRefs = findIdIngredientsTaco(taco.getId());
//
//            for (String idIngredient : ingredientRefs) {
//                taco.setIngredient(ingredientRepository.findById(idIngredient).orElse(null));
//            }
//        }
//
//        return tacos;
//    }
//
//    private List<String> findIdIngredientsTaco(long idTaco) {
//        String query = "SELECT ingredient FROM Ingredient_Ref WHERE taco = ?";
//        return jdbc.query(query, (ResultSet rs, int rowNum) -> rs.getString("ingredient"), idTaco);
//    }

}
