package ru.tikh1y.tacos.repositories;
import ru.tikh1y.tacos.models.TacoOrder;

import java.util.List;

public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);

    TacoOrder update(TacoOrder tacoOrder);

    boolean existsById(Long orderId);

    List<TacoOrder> findOrdersByUserId(Long userId);
}
