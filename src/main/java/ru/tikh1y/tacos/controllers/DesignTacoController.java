package ru.tikh1y.tacos.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.tikh1y.tacos.models.Ingredient;
import ru.tikh1y.tacos.models.Taco;
import ru.tikh1y.tacos.models.TacoOrder;
import ru.tikh1y.tacos.models.Type;
import ru.tikh1y.tacos.repositories.IngredientRepository;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SessionAttributes("tacoOrder")
@RequestMapping("/design")
@Controller
public class DesignTacoController {

    private final IngredientRepository repository;

    public DesignTacoController(IngredientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) {
            log.info("Ошибка");
            return "design";
        }
        tacoOrder.addTaco(taco);
        log.info("Приготовление тако: {}", taco);
        return "redirect:/orders/current";
    }


    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = repository.findAll();

        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder tacoOrder() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
