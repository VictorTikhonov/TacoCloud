package ru.tikh1y.tacos.repositories;

import ru.tikh1y.tacos.models.Taco;

import java.util.List;

public interface TacoRepository {
    List<Taco> findAll();


}
