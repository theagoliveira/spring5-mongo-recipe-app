package guru.springframework.spring5mongorecipeapp.services;

import org.springframework.stereotype.Service;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5mongorecipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.exceptions.NotFoundException;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Mono<Recipe> findById(String id) {
        return recipeReactiveRepository.findById(id)
                                       .switchIfEmpty(
                                           Mono.defer(
                                               () -> Mono.error(
                                                   new NotFoundException(
                                                       "Recipe with ID " + id + " not found."
                                                   )
                                               )
                                           )
                                       );
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return this.findById(id).map(recipe -> {
            var recipeCommand = recipeToRecipeCommand.convert(recipe);

            recipeCommand.getIngredients().forEach(ingredientCommand -> {
                ingredientCommand.setRecipeId(recipeCommand.getId());
            });

            return recipeCommand;
        });
    }

    @Override
    public Flux<Recipe> findAll() {
        log.info("Inside findAll() method of RecipeService");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<RecipeCommand> saveCommand(RecipeCommand command) {
        return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
                                       .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return recipeReactiveRepository.deleteById(id);
    }

}
