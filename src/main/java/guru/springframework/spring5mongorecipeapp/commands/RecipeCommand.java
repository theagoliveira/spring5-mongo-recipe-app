package guru.springframework.spring5mongorecipeapp.commands;

import java.util.ArrayList;
import java.util.List;

import guru.springframework.spring5mongorecipeapp.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {

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
    private List<IngredientCommand> ingredients = new ArrayList<>();
    private Byte[] image;
    private NotesCommand notes;
    private List<CategoryCommand> categories = new ArrayList<>();

}
