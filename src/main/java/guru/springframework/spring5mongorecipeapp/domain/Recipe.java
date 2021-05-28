package guru.springframework.spring5mongorecipeapp.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Recipe {

    private String id;
    private String name;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Difficulty difficulty;
    private Set<Ingredient> ingredients = new HashSet<>();
    private Byte[] image;
    private Notes notes;
    private Set<Category> categories = new HashSet<>();

    public Recipe(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Recipe(String name) {
        this.name = name;
    }

    public Recipe addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
        return this;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
        if (notes != null) {
            notes.setRecipe(this);
        }
    }

    public Optional<Ingredient> getIngredientById(String ingredientId) {
        return this.getIngredients()
                   .stream()
                   .filter(i -> i.getId().equals(ingredientId))
                   .findFirst();
    }

}
