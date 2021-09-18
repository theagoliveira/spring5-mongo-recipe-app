package guru.springframework.spring5mongorecipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Category;
import guru.springframework.spring5mongorecipeapp.domain.Ingredient;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import lombok.Synchronized;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    IngredientToIngredientCommand ingredientToIngredientCommand;
    CategoryToCategoryCommand categoryToCategoryCommand;
    NotesToNotesCommand notesToNotesCommand;

    public RecipeToRecipeCommand(IngredientToIngredientCommand ingredientToIngredientCommand,
                                 CategoryToCategoryCommand categoryToCategoryCommand,
                                 NotesToNotesCommand notesToNotesCommand) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.categoryToCategoryCommand = categoryToCategoryCommand;
        this.notesToNotesCommand = notesToNotesCommand;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe source) {
        if (source == null) {
            return null;
        }

        final var recipeCommand = new RecipeCommand();
        recipeCommand.setId(source.getId());
        recipeCommand.setName(source.getName());
        recipeCommand.setDescription(source.getDescription());
        recipeCommand.setPrepTime(source.getPrepTime());
        recipeCommand.setCookTime(source.getCookTime());
        recipeCommand.setServings(source.getServings());
        recipeCommand.setSource(source.getSource());
        recipeCommand.setUrl(source.getUrl());
        recipeCommand.setDirections(source.getDirections().replaceAll("<(/|)\\s*script\\s*>", ""));
        recipeCommand.setDifficulty(source.getDifficulty());
        for (Ingredient ingredient : source.getIngredients()) {
            recipeCommand.getIngredients().add(ingredientToIngredientCommand.convert(ingredient));
        }
        recipeCommand.setImage(source.getImage());
        recipeCommand.setNotes(notesToNotesCommand.convert(source.getNotes()));
        for (Category category : source.getCategories()) {
            recipeCommand.getCategories().add(categoryToCategoryCommand.convert(category));
        }
        return recipeCommand;
    }

}
