package guru.springframework.spring5mongorecipeapp.commands;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import guru.springframework.spring5mongorecipeapp.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {

    private String id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @Min(0)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(100)
    private Integer servings;

    @URL
    private String url;

    @NotBlank
    private String directions;

    private String source;
    private Difficulty difficulty;
    private List<IngredientCommand> ingredients = new ArrayList<>();
    private Byte[] image;
    private NotesCommand notes;
    private List<CategoryCommand> categories = new ArrayList<>();

}
