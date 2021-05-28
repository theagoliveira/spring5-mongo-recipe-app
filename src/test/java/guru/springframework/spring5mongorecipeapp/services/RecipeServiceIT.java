package guru.springframework.spring5mongorecipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5mongorecipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;

@Disabled
@SpringBootTest
class RecipeServiceIT {

    private static final String NEW_DESCRIPTION = "newDescription";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Disabled
    @Test
    void testSaveOfDescription() {
        // given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        // when
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveCommand(testRecipeCommand);

        // then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(
            testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size()
        );
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());

    }

}
