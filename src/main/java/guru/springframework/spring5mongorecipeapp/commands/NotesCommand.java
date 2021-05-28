package guru.springframework.spring5recipeapp.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotesCommand {

    private Long id;
    private String recipeNotes;

}
