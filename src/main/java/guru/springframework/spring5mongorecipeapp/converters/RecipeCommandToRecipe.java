package guru.springframework.spring5mongorecipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.spring5mongorecipeapp.commands.CategoryCommand;
import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import lombok.Synchronized;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    IngredientCommandToIngredient ingredientCommandToIngredient;
    CategoryCommandToCategory categoryCommandToCategory;
    NotesCommandToNotes notesCommandToNotes;

    public RecipeCommandToRecipe(IngredientCommandToIngredient ingredientCommandToIngredient,
                                 CategoryCommandToCategory categoryCommandToCategory,
                                 NotesCommandToNotes notesCommandToNotes) {
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.categoryCommandToCategory = categoryCommandToCategory;
        this.notesCommandToNotes = notesCommandToNotes;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand source) {
        if (source == null) {
            return null;
        }

        final var recipe = new Recipe();
        recipe.setId(source.getId());
        recipe.setName(source.getName());
        recipe.setDescription(source.getDescription());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setCookTime(source.getCookTime());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setDirections(source.getDirections().replaceAll("<(/|)\\s*script\\s*>", ""));
        recipe.setDifficulty(source.getDifficulty());
        for (IngredientCommand ingredientCommand : source.getIngredients()) {
            recipe.getIngredients().add(ingredientCommandToIngredient.convert(ingredientCommand));
        }
        recipe.setImage(source.getImage());
        recipe.setNotes(notesCommandToNotes.convert(source.getNotes()));
        for (CategoryCommand categoryCommand : source.getCategories()) {
            recipe.getCategories().add(categoryCommandToCategory.convert(categoryCommand));
        }
        return recipe;
    }

}
