package guru.springframework.spring5mongorecipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import guru.springframework.spring5mongorecipeapp.bootstrap.DataLoader;
import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.converters.CategoryCommandToCategory;
import guru.springframework.spring5mongorecipeapp.converters.CategoryToCategoryCommand;
import guru.springframework.spring5mongorecipeapp.converters.IngredientCommandToIngredient;
import guru.springframework.spring5mongorecipeapp.converters.IngredientToIngredientCommand;
import guru.springframework.spring5mongorecipeapp.converters.NotesCommandToNotes;
import guru.springframework.spring5mongorecipeapp.converters.NotesToNotesCommand;
import guru.springframework.spring5mongorecipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5mongorecipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5mongorecipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.spring5mongorecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.repositories.CategoryRepository;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import guru.springframework.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.RecipeReactiveRepository;

@DataMongoTest
class RecipeServiceIT {

    private static final String NEW_DESCRIPTION = "newDescription";

    RecipeService recipeService;

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    CategoryRepository categoryRepository;

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    IngredientToIngredientCommand ingredientToIngredientCommand = new IngredientToIngredientCommand(
        unitOfMeasureToUnitOfMeasureCommand
    );
    CategoryToCategoryCommand categoryToCategoryCommand = new CategoryToCategoryCommand();
    NotesToNotesCommand notesToNotesCommand = new NotesToNotesCommand();
    RecipeToRecipeCommand recipeToRecipeCommand = new RecipeToRecipeCommand(
        ingredientToIngredientCommand, categoryToCategoryCommand, notesToNotesCommand
    );

    UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure = new UnitOfMeasureCommandToUnitOfMeasure();
    IngredientCommandToIngredient ingredientCommandToIngredient = new IngredientCommandToIngredient(
        unitOfMeasureCommandToUnitOfMeasure
    );
    CategoryCommandToCategory categoryCommandToCategory = new CategoryCommandToCategory();
    NotesCommandToNotes notesCommandToNotes = new NotesCommandToNotes();
    RecipeCommandToRecipe recipeCommandToRecipe = new RecipeCommandToRecipe(
        ingredientCommandToIngredient, categoryCommandToCategory, notesCommandToNotes
    );

    @BeforeEach
    void setUp() throws Exception {
        recipeService = new RecipeServiceImpl(
            recipeReactiveRepository, recipeCommandToRecipe, recipeToRecipeCommand
        );

        var dataLoader = new DataLoader(
            recipeRepository, unitOfMeasureRepository, categoryRepository
        );

        dataLoader.run();
    }

    @Test
    void testSaveOfDescription() {
        // given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        // when
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveCommand(testRecipeCommand).block();

        // then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(
            testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size()
        );
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());

    }

}
