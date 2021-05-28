package guru.springframework.spring5mongorecipeapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@NoArgsConstructor
public class Notes {

    private String id;
    private Recipe recipe;
    private String recipeNotes;

    public Notes(String id, String recipeNotes) {
        this.id = id;
        this.recipeNotes = recipeNotes;
    }

}
