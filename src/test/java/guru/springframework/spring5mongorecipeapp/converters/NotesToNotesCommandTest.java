package guru.springframework.spring5mongorecipeapp.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.spring5mongorecipeapp.commands.NotesCommand;
import guru.springframework.spring5mongorecipeapp.domain.Notes;

class NotesToNotesCommandTest {

    public static final String ID = "1";
    public static final String RECIPE_NOTES = "recipeNotes";

    NotesToNotesCommand converter;

    @BeforeEach
    void setUp() {
        converter = new NotesToNotesCommand();
    }

    @Test
    void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new Notes()));
    }

    @Test
    void convert() {
        // given
        Notes notes = new Notes();
        notes.setId(ID);
        notes.setRecipeNotes(RECIPE_NOTES);

        // when
        NotesCommand notesCommand = converter.convert(notes);

        // then
        assertNotNull(notesCommand);
        assertEquals(ID, notesCommand.getId());
        assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
    }

}
