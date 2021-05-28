package guru.springframework.spring5mongorecipeapp.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Notes {

    @Id
    private String id;
    private String recipeNotes;

    public Notes(String id, String recipeNotes) {
        this.id = id;
        this.recipeNotes = recipeNotes;
    }

}
