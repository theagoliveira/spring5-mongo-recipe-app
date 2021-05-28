package guru.springframework.spring5mongorecipeapp.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import guru.springframework.spring5mongorecipeapp.commands.CategoryCommand;
import guru.springframework.spring5mongorecipeapp.domain.Category;

class CategoryCommandToCategoryTest {

    public static final String ID = "1";
    public static final String DESCRIPTION = "description";

    CategoryCommandToCategory converter;

    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        // given
        CategoryCommand command = new CategoryCommand();
        command.setId(ID);
        command.setDescription(DESCRIPTION);

        // when
        Category category = converter.convert(command);

        // then
        assertNotNull(category);
        assertEquals(ID, category.getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }

}
