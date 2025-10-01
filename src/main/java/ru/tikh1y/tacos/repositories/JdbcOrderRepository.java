package ru.tikh1y.tacos.repositories;


import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tikh1y.tacos.RowMappers.TacoOrderRowMapper;
import ru.tikh1y.tacos.models.Ingredient;
import ru.tikh1y.tacos.models.Taco;
import ru.tikh1y.tacos.models.TacoOrder;


import java.sql.Types;
import java.util.*;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {
        // Подготовка SQL-запроса для сохранения заказа
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco_Order "
                        + "(delivery_name, delivery_street, delivery_city, "
                        + "delivery_state, delivery_zip, cc_number, "
                        + "cc_expiration, cc_cvv, place_at, user_id) "
                        + "values (?,?,?,?,?,?,?,?,?,?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.BIGINT
        );

        pscf.setReturnGeneratedKeys(true);

        // Установка даты создания заказа
        tacoOrder.setPlaceAt(new Date());

        // Создание PreparedStatementCreator
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(
                tacoOrder.getDeliveryName(),
                tacoOrder.getDeliveryStreet(),
                tacoOrder.getDeliveryCity(),
                tacoOrder.getDeliveryState(),
                tacoOrder.getDeliveryZip(),
                tacoOrder.getCcNumber(),
                tacoOrder.getCcExpiration(),
                tacoOrder.getCcCVV(),
                tacoOrder.getPlaceAt(),
                tacoOrder.getUserId())
        );

        // Создание объекта для хранения сгенерированных ключей
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        // Получение сгенерированного ID заказа
        Map<String, Object> keys = Objects.requireNonNull(keyHolder.getKeys());
        long orderId = ((Number) keys.get("id")).longValue();  // 'id' - это имя столбца с первичным ключом
        tacoOrder.setId(orderId);

        // Сохранение каждого тако в заказе
        List<Taco> tacos = tacoOrder.getTacos();
        int i = 0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }

        return tacoOrder;
    }

    private long saveTaco(Long orderId, int orderKey, Taco taco) {
        // Подготовка SQL-запроса для сохранения тако
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco "
                        + "(name, created_at, taco_order, taco_order_key) "
                        + "values (?, ?, ?, ?)",
                Types.VARCHAR, Types.TIMESTAMP, Types.BIGINT, Types.BIGINT
        );
        pscf.setReturnGeneratedKeys(true);

        // Создание PreparedStatementCreator
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(
                taco.getName(),
                taco.getCreatedAt(),
                orderId,
                orderKey));

        // Сохранение тако и получение сгенерированного ID
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        Map<String, Object> keys = Objects.requireNonNull(keyHolder.getKeys());
        long tacoId = ((Number) keys.get("id")).longValue();  // 'id' - это имя столбца с первичным ключом
        taco.setId(tacoId);

        // Сохранение связей ингредиентов для тако
        saveIngredientRefs(tacoId, taco.getIngredients());

        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<Ingredient> ingredients) {
        int key = 0;
        for (Ingredient ingredient : ingredients) {
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key) "
                            + "values (?, ?, ?)",
                    ingredient.getId(), tacoId, key++);
        }
    }



    @Transactional
    @Override
    public TacoOrder update(TacoOrder tacoOrder) {
        String updateQuery = "UPDATE Taco_Order SET delivery_name = ?, delivery_street = ?, " +
                "delivery_city = ?, delivery_state = ?, delivery_zip = ?, " +
                "cc_number = ?, cc_expiration = ?, cc_cvv = ? WHERE id = ?";

        jdbcOperations.update(updateQuery,
                tacoOrder.getDeliveryName(),
                tacoOrder.getDeliveryStreet(),
                tacoOrder.getDeliveryCity(),
                tacoOrder.getDeliveryState(),
                tacoOrder.getDeliveryZip(),
                tacoOrder.getCcNumber(),
                tacoOrder.getCcExpiration(),
                tacoOrder.getCcCVV(),
                tacoOrder.getId());

        // Обновляем тако, если они есть в заказе
        List<Taco> tacos = tacoOrder.getTacos();
        if (tacos != null) {
            for (int i = 0; i < tacos.size(); i++) {
                updateTaco(tacoOrder.getId(), i, tacos.get(i));
            }
        }

        return tacoOrder;
    }

    private void updateTaco(Long orderId, int orderKey, Taco taco) {
        String updateQuery = "UPDATE Taco SET name = ?, created_at = ? WHERE id = ?";

        jdbcOperations.update(updateQuery,
                taco.getName(),
                taco.getCreatedAt(),
                taco.getId());

        // Обновляем ингредиенты для тако
        deleteIngredientRefs(taco.getId());  // Сначала удаляем старые связи ингредиентов
        saveIngredientRefs(taco.getId(), taco.getIngredients());  // Добавляем новые связи ингредиентов
    }

    private void deleteIngredientRefs(long tacoId) {
        jdbcOperations.update("DELETE FROM Ingredient_Ref WHERE taco = ?", tacoId);
    }

    // Реализация метода для проверки существования заказа
    @Override
    public boolean existsById(Long orderId) {
        String existsQuery = "SELECT count(*) FROM Taco_Order WHERE id = ?";
        Integer count = jdbcOperations.queryForObject(existsQuery, Integer.class, orderId);
        return count != null && count > 0;
    }



    public List<TacoOrder> findOrdersByUserId(Long userId) {
        String sql = "SELECT * FROM Taco_Order WHERE user_id = ?";
        // Передаем jdbcTemplate в TacoOrderRowMapper
        return jdbcOperations.query(sql, new Object[]{userId}, new TacoOrderRowMapper(jdbcOperations));
    }
}