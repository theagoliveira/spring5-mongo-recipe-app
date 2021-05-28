package guru.springframework.spring5mongorecipeapp.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.spring5mongorecipeapp.commands.NotesCommand;
import guru.springframework.spring5mongorecipeapp.domain.Notes;

class NotesCommandToNotesTest {

    public static final String ID = "1";
    public static final String RECIPE_NOTES = "recipeNotes";

    NotesCommandToNotes converter;

    @BeforeEach
    void setUp() {
        converter = new NotesCommandToNotes();
    }

    @Test
    void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new NotesCommand()));
    }

    @Test
    void convert() {
        // given
        NotesCommand command = new NotesCommand();
        command.setId(ID);
        command.setRecipeNotes(RECIPE_NOTES);

        // when
        Notes notes = converter.convert(command);

        // then
        assertNotNull(notes);
        assertEquals(ID, notes.getId());
        assertEquals(RECIPE_NOTES, notes.getRecipeNotes());
    }

}
