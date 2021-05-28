package guru.springframework.spring5mongorecipeapp.domain;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(exclude = {"recipes"})
@NoArgsConstructor
public class Category {

    private String id;
    private String description;
    private Set<Recipe> recipes;

    public Category(String id, String description) {
        this.id = id;
        this.description = description;
    }

}
