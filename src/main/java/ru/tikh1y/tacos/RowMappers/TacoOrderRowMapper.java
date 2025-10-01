package ru.tikh1y.tacos.RowMappers;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import ru.tikh1y.tacos.models.Ingredient;
import ru.tikh1y.tacos.models.Taco;
import ru.tikh1y.tacos.models.TacoOrder;
import ru.tikh1y.tacos.models.Type;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TacoOrderRowMapper implements RowMapper<TacoOrder> {
    private final JdbcOperations jdbcOperations;

    public TacoOrderRowMapper(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public TacoOrder mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        TacoOrder order = new TacoOrder();
        order.setId(rs.getLong("id"));
        order.setPlaceAt(rs.getTimestamp("place_at"));
        order.setDeliveryName(rs.getString("delivery_name"));
        order.setDeliveryStreet(rs.getString("delivery_street"));
        order.setDeliveryCity(rs.getString("delivery_city"));
        order.setDeliveryState(rs.getString("delivery_state"));
        order.setDeliveryZip(rs.getString("delivery_zip"));
        order.setUserId(rs.getLong("user_id"));
        // Здесь можно добавить маппинг для списка тако, например
        order.setTacos(getTacosByOrderId(order.getId()));
        return order;
    }

    private List<Taco> getTacosByOrderId(Long orderId) {
        String query = "SELECT t.id AS taco_id, t.name AS taco_name, t.created_at, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.type AS ingredient_type " +
                "FROM Taco t " +
                "INNER JOIN Ingredient_Ref ir ON t.id = ir.taco " +
                "INNER JOIN Ingredient i ON ir.ingredient = i.id " +
                "WHERE t.taco_order = ?"; // Используем поле taco_order для связи с заказом

        Map<Long, Taco> tacoMap = new HashMap<>();

        jdbcOperations.query(query, new Object[]{orderId}, rs -> {
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


}
