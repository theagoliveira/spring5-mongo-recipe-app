package guru.springframework.spring5mongorecipeapp.services;

import java.util.Set;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;

public interface RecipeService {

    Recipe findById(String id);

    RecipeCommand findCommandById(String id);

    Set<Recipe> findAll();

    RecipeCommand saveCommand(RecipeCommand command);

    void deleteById(String id);

}
