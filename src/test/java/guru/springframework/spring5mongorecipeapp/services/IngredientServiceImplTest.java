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
import java.util.Optional;

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
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import guru.springframework.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;

class IngredientServiceImplTest {

    private static final String RECIPE_ID = "1";
    private static final String INGREDIENT_ID = "2";
    private static final String COMMAND_ID = "3";
    private static final String UOM_ID = "4";
    private static final String DESCRIPTION = "description";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(4.0);

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

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
            recipeRepository, unitOfMeasureRepository, ingredientToIngredientCommand,
            ingredientCommandToIngredient
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
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));
        IngredientCommand command = ingredientService.findCommandByIdAndRecipeId(
            INGREDIENT_ID, RECIPE_ID
        );

        // then
        assertNull(command.getRecipeId());
        assertNotNull(command);
        assertEquals(INGREDIENT_ID, command.getId());
        verify(recipeRepository).findById(anyString());
    }

    @Test
    void saveCommandWithoutRecipe() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(new UnitOfMeasureCommand(UOM_ID, DESCRIPTION));

        // when
        when(recipeRepository.findById(anyString())).thenReturn(Optional.empty());
        IngredientCommand savedCommand = ingredientService.saveCommand(command);

        // then
        assertNull(savedCommand);
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository, never()).save(any());
        verify(unitOfMeasureRepository, never()).findById(anyString());
    }

    @Test
    void saveCommandWithRecipeWithoutCommandId() {
        // given
        var command = new IngredientCommand();
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(new UnitOfMeasureCommand(UOM_ID, DESCRIPTION));

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(RECIPE_ID);
        var ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setAmount(AMOUNT);
        ingredient.setUom(new UnitOfMeasure(UOM_ID, DESCRIPTION));
        savedRecipe.addIngredient(ingredient);

        // when
        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        IngredientCommand savedCommand = ingredientService.saveCommand(command);

        // then
        assertNull(savedCommand.getRecipeId());
        assertNotNull(savedCommand.getId());
        assertEquals(INGREDIENT_ID, savedCommand.getId());
        assertEquals(DESCRIPTION, savedCommand.getDescription());
        assertEquals(AMOUNT, savedCommand.getAmount());
        assertEquals(UOM_ID, savedCommand.getUom().getId());
        assertEquals(DESCRIPTION, savedCommand.getUom().getDescription());
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
        verify(unitOfMeasureRepository, never()).findById(anyString());
    }

    @Test
    void saveCommandWithRecipeWithoutOptIngredientWithoutSavedIngredient() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(new UnitOfMeasureCommand(UOM_ID, DESCRIPTION));

        Recipe recipe = new Recipe();
        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        Recipe savedRecipe = new Recipe();

        // when
        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        IngredientCommand savedCommand = ingredientService.saveCommand(command);

        // then
        assertNull(savedCommand);
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
        verify(unitOfMeasureRepository, never()).findById(anyString());
    }

    @Test
    void saveCommandWithRecipeWithOptIngredientWithoutSavedIngredient() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(new UnitOfMeasureCommand(UOM_ID, DESCRIPTION));

        var ingredient = new Ingredient();
        ingredient.setId(COMMAND_ID);

        Recipe recipe = new Recipe();
        Optional<Recipe> optionalRecipe = Optional.of(recipe);
        recipe.addIngredient(ingredient);

        Recipe savedRecipe = new Recipe();

        // when
        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        when(unitOfMeasureRepository.findById(anyString())).thenReturn(
            Optional.of(new UnitOfMeasure(UOM_ID, DESCRIPTION))
        );
        IngredientCommand savedCommand = ingredientService.saveCommand(command);

        // then
        assertNull(savedCommand);
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
        verify(unitOfMeasureRepository).findById(anyString());
    }

    @Test
    void saveCommandWithRecipeWithoutOptIngredientWithSavedIngredient() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(new UnitOfMeasureCommand(UOM_ID, DESCRIPTION));

        var ingredient = new Ingredient();
        ingredient.setId(COMMAND_ID);

        Recipe recipe = new Recipe();
        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(ingredient);

        // when
        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        IngredientCommand savedCommand = ingredientService.saveCommand(command);

        // then
        assertEquals(COMMAND_ID, savedCommand.getId());
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
        verify(unitOfMeasureRepository, never()).findById(anyString());
    }

    @Test
    void saveCommandWithRecipeWithOptIngredientWithSavedIngredient() {
        // given
        var command = new IngredientCommand();
        command.setId(COMMAND_ID);
        command.setRecipeId(RECIPE_ID);
        command.setDescription(DESCRIPTION);
        command.setAmount(AMOUNT);
        command.setUom(new UnitOfMeasureCommand(UOM_ID, DESCRIPTION));

        var ingredient = new Ingredient();
        ingredient.setId(COMMAND_ID);

        Recipe recipe = new Recipe();
        Optional<Recipe> optionalRecipe = Optional.of(recipe);
        recipe.addIngredient(ingredient);

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(ingredient);

        // when
        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        when(unitOfMeasureRepository.findById(anyString())).thenReturn(
            Optional.of(new UnitOfMeasure(UOM_ID, DESCRIPTION))
        );
        IngredientCommand savedCommand = ingredientService.saveCommand(command);

        // then
        assertEquals(COMMAND_ID, savedCommand.getId());
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
        verify(unitOfMeasureRepository).findById(anyString());
    }

    @Test
    void deleteById() {
        // given
        var ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);

        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.addIngredient(ingredient);

        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        // when
        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);
        ingredientService.deleteByIdAndRecipeId(INGREDIENT_ID, RECIPE_ID);

        // then
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
    }

}
