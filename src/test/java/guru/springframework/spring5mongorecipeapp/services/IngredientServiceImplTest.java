package guru.springframework.spring5mongorecipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.converters.IngredientCommandToIngredient;
import guru.springframework.spring5mongorecipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.spring5mongorecipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.spring5mongorecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.domain.Ingredient;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Mono;

class IngredientServiceImplTest {

    private static final String RECIPE_ID = "1";
    private static final String INGREDIENT_ID = "2";
    private static final String COMMAND_ID = "3";
    private static final String UOM_ID = "4";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(4.0);
    private static final UnitOfMeasure UOM = new UnitOfMeasure(UOM_ID, DESCRIPTION);
    private static final UnitOfMeasureCommand UOM_COMMAND = new UnitOfMeasureCommand(
        UOM_ID, DESCRIPTION
    );

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure = new UnitOfMeasureCommandToUnitOfMeasure();

    IngredientToIngredientCommand ingredientToIngredientCommand = new IngredientToIngredientCommand(
        unitOfMeasureToUnitOfMeasureCommand
    );
    IngredientCommandToIngredient ingredientCommandToIngredient = new IngredientCommandToIngredient(
        unitOfMeasureCommandToUnitOfMeasure
    );

    IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(
            recipeReactiveRepository, unitOfMeasureReactiveRepository,
            ingredientToIngredientCommand, ingredientCommandToIngredient
        );
    }

    @Test
    void findCommandByIdAndRecipeId() {
        // given
        var ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);

        var recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.getIngredients().add(ingredient);

        var returnCommand = new IngredientCommand();
        returnCommand.setId(INGREDIENT_ID);
        returnCommand.setRecipeId(RECIPE_ID);

        // when
        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        IngredientCommand command = ingredientService.findCommandByIdAndRecipeId(
            INGREDIENT_ID, RECIPE_ID
        ).block();

        // then
        assertNotNull(command);
        assertEquals(RECIPE_ID, command.getRecipeId());
        assertEquals(INGREDIENT_ID, command.getId());
        verify(recipeReactiveRepository).findById(anyString());
    }

    @Test
    void saveCommandWithoutRecipe() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(UOM_COMMAND);

        // when
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(UOM));
        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.empty());
        IngredientCommand savedCommand = ingredientService.saveCommand(command).block();

        // then
        assertNotNull(savedCommand);
        assertNull(savedCommand.getId());
        verify(unitOfMeasureReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository, never()).save(any());
    }

    @Test
    void saveCommandWithRecipeWithoutCommandId() {
        // given
        var command = new IngredientCommand();
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(UOM_COMMAND);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        Mono<Recipe> monoRecipe = Mono.just(recipe);

        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(RECIPE_ID);
        var ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setAmount(AMOUNT);
        ingredient.setUom(new UnitOfMeasure(UOM_ID, DESCRIPTION));
        savedRecipe.addIngredient(ingredient);

        // when
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(UOM));
        when(recipeReactiveRepository.findById(anyString())).thenReturn(monoRecipe);
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));
        IngredientCommand savedCommand = ingredientService.saveCommand(command).block();

        // then
        assertNotNull(savedCommand.getId());
        assertEquals(RECIPE_ID, savedCommand.getRecipeId());
        assertEquals(INGREDIENT_ID, savedCommand.getId());
        assertEquals(DESCRIPTION, savedCommand.getDescription());
        assertEquals(AMOUNT, savedCommand.getAmount());
        assertEquals(UOM_ID, savedCommand.getUom().getId());
        assertEquals(DESCRIPTION, savedCommand.getUom().getDescription());
        verify(unitOfMeasureReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }

    @Test
    void saveCommandWithRecipeWithoutOptIngredientWithoutSavedIngredient() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(UOM_COMMAND);

        Recipe recipe = new Recipe();
        Mono<Recipe> monoRecipe = Mono.just(recipe);

        Recipe savedRecipe = new Recipe();

        // when
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(UOM));
        when(recipeReactiveRepository.findById(anyString())).thenReturn(monoRecipe);
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));
        Mono<IngredientCommand> savedCommand = ingredientService.saveCommand(command);

        // then
        assertNull(savedCommand);
        verify(unitOfMeasureReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }

    @Test
    void saveCommandWithRecipeWithOptIngredientWithoutSavedIngredient() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(UOM_COMMAND);

        var ingredient = new Ingredient();
        ingredient.setId(COMMAND_ID);

        Recipe recipe = new Recipe();
        Mono<Recipe> monoRecipe = Mono.just(recipe);
        recipe.addIngredient(ingredient);

        Recipe savedRecipe = new Recipe();

        // when
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(UOM));
        when(recipeReactiveRepository.findById(anyString())).thenReturn(monoRecipe);
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));
        Mono<IngredientCommand> savedCommand = ingredientService.saveCommand(command);

        // then
        assertNull(savedCommand);
        verify(unitOfMeasureReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }

    @Test
    void saveCommandWithRecipeWithoutOptIngredientWithSavedIngredient() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(UOM_COMMAND);

        var ingredient = new Ingredient();
        ingredient.setId(COMMAND_ID);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setAmount(AMOUNT);
        ingredient.setUom(UOM);

        Recipe recipe = new Recipe();
        Mono<Recipe> monoRecipe = Mono.just(recipe);

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(ingredient);

        // when
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(UOM));
        when(recipeReactiveRepository.findById(anyString())).thenReturn(monoRecipe);
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));
        IngredientCommand savedCommand = ingredientService.saveCommand(command).block();

        // then
        assertEquals(COMMAND_ID, savedCommand.getId());
        verify(unitOfMeasureReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }

    @Test
    void saveCommandWithRecipeWithOptIngredientWithSavedIngredient() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(UOM_COMMAND);

        var ingredient = new Ingredient();
        ingredient.setId(COMMAND_ID);

        Recipe recipe = new Recipe();
        Mono<Recipe> monoRecipe = Mono.just(recipe);
        recipe.addIngredient(ingredient);

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(ingredient);

        // when
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(UOM));
        when(recipeReactiveRepository.findById(anyString())).thenReturn(monoRecipe);
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(savedRecipe));
        IngredientCommand savedCommand = ingredientService.saveCommand(command).block();

        // then
        assertEquals(COMMAND_ID, savedCommand.getId());
        verify(unitOfMeasureReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }

    @Test
    void deleteById() {
        // given
        var ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.addIngredient(ingredient);

        // when
        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));
        ingredientService.deleteByIdAndRecipeId(INGREDIENT_ID, RECIPE_ID);

        // then
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository).save(any(Recipe.class));
    }

}
