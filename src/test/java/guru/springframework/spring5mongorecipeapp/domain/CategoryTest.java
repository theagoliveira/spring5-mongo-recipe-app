package guru.springframework.spring5mongorecipeapp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

    Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void getId() {
        String idValue = "4";

        category.setId(idValue);

        assertEquals(idValue, category.getId());
    }

}
