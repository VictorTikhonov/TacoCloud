package ru.tikh1y.tacos.repositories;
import ru.tikh1y.tacos.models.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository{
    List<Ingredient> findAll();
    Optional<Ingredient> findById(String id);
    Ingredient save(Ingredient ingredient);
}
