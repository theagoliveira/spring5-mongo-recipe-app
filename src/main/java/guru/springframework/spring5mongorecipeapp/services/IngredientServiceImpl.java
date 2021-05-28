package guru.springframework.spring5mongorecipeapp.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import guru.springframework.spring5mongorecipeapp.converters.IngredientCommandToIngredient;
import guru.springframework.spring5mongorecipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import guru.springframework.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private static final String RECIPE_WITH_ID = "Recipe with ID ";
    private static final String NOT_FOUND = " not found.";

    RecipeRepository recipeRepository;
    UnitOfMeasureRepository unitOfMeasureRepository;
    IngredientToIngredientCommand ingredientToIngredientCommand;
    IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository,
                                 UnitOfMeasureRepository unitOfMeasureRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public IngredientCommand findCommandByIdAndRecipeId(String id, String recipeId) {
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

        return ingredientToIngredientCommand.convert(optIngredient.get());
    }

    @Override
    public IngredientCommand saveCommand(IngredientCommand command) {
        String id = command.getId();
        String recipeId = command.getRecipeId();
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (!optionalRecipe.isPresent()) {
            log.error(RECIPE_WITH_ID + recipeId + NOT_FOUND);
            return null;
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
                    unitOfMeasureRepository.findById(
                        command.getUom().getId()
                    ).orElseThrow(() -> new RuntimeException("Unit of Measure not found."))
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
            }

            return ingredientCommandSaved;
        }
    }

    @Override
    public void deleteByIdAndRecipeId(String id, String recipeId) {
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
    }

}
