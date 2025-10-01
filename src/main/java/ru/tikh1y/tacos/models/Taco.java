package ru.tikh1y.tacos.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Taco {
    private Long id;
    private Date createdAt = new Date();

    @NotNull
    @Size(min = 5, message = "Длина имени должна составлять не менее 5 символов")
    private String name;

    @NotNull
    @Size(min = 1, message = "Вы должны выбрать как минимум 1 ингредиент")
    private List<Ingredient> ingredients;

    public Taco() {
        this.ingredients = new ArrayList<>();
    }


    public Taco(Long id, Date createdAt, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setIngredient(Ingredient ingredient)
    {
        this.ingredients.add(ingredient);
    }
}
