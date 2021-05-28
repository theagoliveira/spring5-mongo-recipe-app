package guru.springframework.spring5mongorecipeapp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.services.RecipeServiceImpl;

class IndexControllerTest {

    IndexController indexController;

    @Mock
    RecipeServiceImpl recipeService;

    @Mock
    Model model;

    @Captor
    ArgumentCaptor<Set<Recipe>> argumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    void getIndexPage() {
        // given
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe("Recipe 1"));
        recipes.add(new Recipe("Recipe 2"));

        // when
        when(recipeService.findAll()).thenReturn(recipes);

        // then
        assertEquals("index", indexController.getIndexPage(model));
        verify(recipeService).findAll();
        verify(model).addAttribute(eq("recipes"), argumentCaptor.capture());

        Set<Recipe> capturedSet = argumentCaptor.getValue();
        assertEquals(2, capturedSet.size());
    }

    @Test
    void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
    }

}
