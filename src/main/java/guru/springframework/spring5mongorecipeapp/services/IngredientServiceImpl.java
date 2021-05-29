package guru.springframework.spring5mongorecipeapp.services;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import guru.springframework.spring5mongorecipeapp.converters.IngredientCommandToIngredient;
import guru.springframework.spring5mongorecipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private static final String RECIPE_WITH_ID = "Recipe with ID ";
    private static final String NOT_FOUND = " not found.";

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand,
                                 IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public Mono<IngredientCommand> findCommandByIdAndRecipeId(String id, String recipeId) {
        return recipeReactiveRepository.findById(recipeId)
                                       .flatMapIterable(Recipe::getIngredients)
                                       .filter(i -> i.getId().equalsIgnoreCase(id))
                                       .single()
                                       .map(i -> {
                                           var command = ingredientToIngredientCommand.convert(i);
                                           command.setRecipeId(recipeId);
                                           return command;
                                       });
    }

    @Override
    public Mono<IngredientCommand> saveCommand(IngredientCommand command) {
        String id = command.getId();

        if (id != null && id.equals("")) {
            id = null;
            command.setId(id);
        }

        String recipeId = command.getRecipeId();
        var recipe = recipeReactiveRepository.findById(recipeId).block();
        var uom = unitOfMeasureReactiveRepository.findById(command.getUom().getId()).block();

        if (uom == null) {
            new RuntimeException("UOM not found.");
        }

        if (recipe == null) {
            log.error(RECIPE_WITH_ID + recipeId + NOT_FOUND);
            return Mono.just(new IngredientCommand());
        } else {
            var optIngredient = recipe.getIngredientById(id);

            if (optIngredient.isPresent()) {
                var ingredient = optIngredient.get();
                ingredient.setDescription(command.getDescription());
                ingredient.setAmount(command.getAmount());
                ingredient.setUom(uom);
            } else {
                var ingredient = ingredientCommandToIngredient.convert(command);
                if (ingredient != null) {
                    id = UUID.randomUUID().toString();
                    ingredient.setId(id);
                    ingredient.setUom(uom);
                    recipe.addIngredient(ingredient);
                }
            }

            var savedRecipe = recipeReactiveRepository.save(recipe).block();
            var savedIngredient = savedRecipe.getIngredientById(id);

            if (!savedIngredient.isPresent()) {
                savedIngredient = savedRecipe.getIngredients()
                                             .stream()
                                             .filter(Objects::nonNull)
                                             .filter(
                                                 i -> (i.getDescription() != null)
                                                         && i.getDescription()
                                                             .equals(command.getDescription())
                                             )
                                             .filter(
                                                 i -> (i.getAmount() != null)
                                                         && i.getAmount()
                                                             .equals(command.getAmount())
                                             )
                                             .filter(
                                                 i -> (i.getUom() != null)
                                                         && (i.getUom().getId() != null)
                                                         && i.getUom()
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
        var recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe != null) {
            var optionalIngredient = recipe.getIngredientById(id);

            if (optionalIngredient.isPresent()) {
                var ingredient = optionalIngredient.get();
                recipe.getIngredients().remove(ingredient);
                recipeReactiveRepository.save(recipe).block();
            } else {
                log.debug("Ingredient with ID " + id + NOT_FOUND);
            }
        } else {
            log.debug(RECIPE_WITH_ID + id + NOT_FOUND);
        }

        return Mono.empty();
    }

}
