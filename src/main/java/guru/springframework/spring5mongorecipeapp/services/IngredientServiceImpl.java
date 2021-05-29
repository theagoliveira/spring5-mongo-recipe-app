package guru.springframework.spring5mongorecipeapp.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import guru.springframework.spring5mongorecipeapp.converters.IngredientCommandToIngredient;
import guru.springframework.spring5mongorecipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private static final String RECIPE_WITH_ID = "Recipe with ID ";
    private static final String NOT_FOUND = " not found.";

    RecipeRepository recipeRepository;
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    IngredientToIngredientCommand ingredientToIngredientCommand;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public Mono<IngredientCommand> findCommandByIdAndRecipeId(String id, String recipeId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            log.error(RECIPE_WITH_ID + recipeId + NOT_FOUND);
            return null;
        }

        var recipe = optionalRecipe.get();
        var optIngredient = recipe.getIngredientById(id);

        if (!optIngredient.isPresent()) {
            log.error("IngredientCommand with ID " + id + NOT_FOUND);
            return null;
        }

        return Mono.just(ingredientToIngredientCommand.convert(optIngredient.get()));
    }

    @Override
    public Mono<IngredientCommand> saveCommand(IngredientCommand command) {
        String id = command.getId();
        String recipeId = command.getRecipeId();
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            log.error(RECIPE_WITH_ID + recipeId + NOT_FOUND);
            return Mono.just(new IngredientCommand());
        } else {
            var recipe = optionalRecipe.get();
            var optIngredient = recipe.getIngredientById(id);

            if (!optIngredient.isPresent()) {
                var ingredient = ingredientCommandToIngredient.convert(command);
                if (ingredient != null) {
                    recipe.addIngredient(ingredient);
                }
            } else {
                var ingredient = optIngredient.get();
                ingredient.setDescription(command.getDescription());
                ingredient.setAmount(command.getAmount());
                ingredient.setUom(
                    unitOfMeasureReactiveRepository.findById(command.getUom().getId())
                                                   .onErrorReturn(null)
                                                   .block()
                );
            }

            var savedRecipe = recipeRepository.save(recipe);
            var savedIngredient = savedRecipe.getIngredientById(id);

            if (!savedIngredient.isPresent()) {
                savedIngredient = savedRecipe.getIngredients()
                                             .stream()
                                             .filter(
                                                 i -> i.getDescription()
                                                       .equals(command.getDescription())
                                             )
                                             .filter(i -> i.getAmount().equals(command.getAmount()))
                                             .filter(
                                                 i -> i.getUom()
                                                       .getId()
                                                       .equals(command.getUom().getId())
                                             )
                                             .findFirst();
            }

            var ingredientCommandSaved = ingredientToIngredientCommand.convert(
                savedIngredient.orElse(null)
            );

            if (ingredientCommandSaved != null) {
                ingredientCommandSaved.setRecipeId(recipe.getId());
                return Mono.just(ingredientCommandSaved);
            } else {
                return null;
            }
        }
    }

    @Override
    public Mono<Void> deleteByIdAndRecipeId(String id, String recipeId) {
        var optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isPresent()) {
            var recipe = optionalRecipe.get();
            var optionalIngredient = recipe.getIngredientById(id);

            if (optionalIngredient.isPresent()) {
                var ingredient = optionalIngredient.get();
                recipe.getIngredients().remove(ingredient);
                recipeRepository.save(recipe);
            } else {
                log.debug("Ingredient with ID " + id + NOT_FOUND);
            }
        } else {
            log.debug(RECIPE_WITH_ID + id + NOT_FOUND);
        }

        return Mono.empty();
    }

}
