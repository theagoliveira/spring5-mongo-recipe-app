package guru.springframework.spring5mongorecipeapp.services;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findCommandByIdAndRecipeId(String id, String recipeId);

    IngredientCommand saveCommand(IngredientCommand command);

    void deleteByIdAndRecipeId(String id, String recipeId);

}
