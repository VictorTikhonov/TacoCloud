package ru.tikh1y.tacos.components;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.tikh1y.tacos.models.Ingredient;
import ru.tikh1y.tacos.repositories.IngredientRepository;


@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private final IngredientRepository repository;

    public IngredientByIdConverter(IngredientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ingredient convert(String id) {
        return this.repository.findById(id).orElse(null);
    }
}
