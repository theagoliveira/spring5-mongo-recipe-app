package guru.springframework.spring5recipeapp.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.NotesCommand;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.domain.Difficulty;
import guru.springframework.spring5recipeapp.domain.Recipe;

class RecipeCommandToRecipeTest {

    public static final Long ID = 1L;
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final Integer PREP_TIME = 1;
    public static final Integer COOK_TIME = 2;
    public static final Integer SERVINGS = 3;
    public static final String SOURCE = "source";
    public static final String URL = "url";
    public static final String DIRECTIONS = "directions";
    public static final Difficulty DIFFICULTY = Difficulty.HARD;
    public static final Long NOTES_ID = 5L;
    public static final String RECIPE_NOTES = "recipeNotes";
    public static final NotesCommand NOTES = new NotesCommand(NOTES_ID, RECIPE_NOTES);
    private static final String IMAGE_TEXT = "imageText";
    private static final byte[] IMAGE_TEXT_BYTES = IMAGE_TEXT.getBytes();
    public static final Long INGREDIENT1_ID = 8L;
    public static final String INGREDIENT1_DESCRIPTION = "ingredient1Description";
    public static final BigDecimal INGREDIENT1_AMOUNT = BigDecimal.valueOf(1.5);
    public static final IngredientCommand INGREDIENT1 = new IngredientCommand(
        INGREDIENT1_ID, INGREDIENT1_DESCRIPTION, INGREDIENT1_AMOUNT
    );
    public static final Long INGREDIENT2_ID = 9L;
    public static final String INGREDIENT2_DESCRIPTION = "ingredient2Description";
    public static final BigDecimal INGREDIENT2_AMOUNT = BigDecimal.valueOf(3.5);
    public static final IngredientCommand INGREDIENT2 = new IngredientCommand(
        INGREDIENT2_ID, INGREDIENT2_DESCRIPTION, INGREDIENT2_AMOUNT
    );
    public static final Long CATEGORY1_ID = 10L;
    public static final String CATEGORY1_DESCRIPTION = "category1Description";
    public static final CategoryCommand CATEGORY1 = new CategoryCommand(
        CATEGORY1_ID, CATEGORY1_DESCRIPTION
    );
    public static final Long CATEGORY2_ID = 11L;
    public static final String CATEGORY2_DESCRIPTION = "category2Description";
    public static final CategoryCommand CATEGORY2 = new CategoryCommand(
        CATEGORY2_ID, CATEGORY2_DESCRIPTION
    );

    RecipeCommandToRecipe converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeCommandToRecipe(
            new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
            new CategoryCommandToCategory(), new NotesCommandToNotes()
        );
    }

    @Test
    void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new RecipeCommand()));
    }

    @Test
    void convert() {
        // given
        RecipeCommand command = new RecipeCommand();
        command.setId(ID);
        command.setName(NAME);
        command.setDescription(DESCRIPTION);
        command.setPrepTime(PREP_TIME);
        command.setCookTime(COOK_TIME);
        command.setServings(SERVINGS);
        command.setSource(SOURCE);
        command.setUrl(URL);
        command.setDirections(DIRECTIONS);
        command.setDifficulty(DIFFICULTY);
        command.setNotes(NOTES);

        var boxedBytes = new Byte[IMAGE_TEXT_BYTES.length];
        var i = 0;
        for (byte b : IMAGE_TEXT_BYTES) {
            boxedBytes[i++] = b;
        }

        command.setImage(boxedBytes);

        command.getIngredients().add(INGREDIENT1);
        command.getIngredients().add(INGREDIENT2);
        command.getCategories().add(CATEGORY1);
        command.getCategories().add(CATEGORY2);

        // when
        Recipe recipe = converter.convert(command);

        // then
        assertNotNull(recipe);
        assertNotNull(recipe.getNotes());
        assertNotNull(recipe.getIngredients());
        assertNotNull(recipe.getCategories());
        assertEquals(ID, recipe.getId());
        assertEquals(NAME, recipe.getName());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(IMAGE_TEXT_BYTES.length, recipe.getImage().length);
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(RECIPE_NOTES, recipe.getNotes().getRecipeNotes());
        assertEquals(2, recipe.getIngredients().size());
        assertEquals(2, recipe.getCategories().size());
    }

    @Test
    void convertWithoutNotesAndSets() {
        // given
        RecipeCommand command = new RecipeCommand();
        command.setId(ID);
        command.setName(NAME);
        command.setDescription(DESCRIPTION);
        command.setPrepTime(PREP_TIME);
        command.setCookTime(COOK_TIME);
        command.setServings(SERVINGS);
        command.setSource(SOURCE);
        command.setUrl(URL);
        command.setDirections(DIRECTIONS);
        command.setDifficulty(DIFFICULTY);

        var boxedBytes = new Byte[IMAGE_TEXT_BYTES.length];
        var i = 0;
        for (byte b : IMAGE_TEXT_BYTES) {
            boxedBytes[i++] = b;
        }

        command.setImage(boxedBytes);

        // when
        Recipe recipe = converter.convert(command);

        // then
        assertNotNull(recipe);
        assertNull(recipe.getNotes());
        assertNotNull(recipe.getIngredients());
        assertNotNull(recipe.getCategories());
        assertEquals(ID, recipe.getId());
        assertEquals(NAME, recipe.getName());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(IMAGE_TEXT_BYTES.length, recipe.getImage().length);
        assertEquals(0, recipe.getIngredients().size());
        assertEquals(0, recipe.getCategories().size());
    }

}
