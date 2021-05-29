package guru.springframework.spring5mongorecipeapp.services;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {

    Mono<IngredientCommand> findCommandByIdAndRecipeId(String id, String recipeId);

    Mono<IngredientCommand> saveCommand(IngredientCommand command);

    Mono<Void> deleteByIdAndRecipeId(String id, String recipeId);

}
