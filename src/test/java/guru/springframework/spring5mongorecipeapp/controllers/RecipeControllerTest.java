package guru.springframework.spring5mongorecipeapp.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.exceptions.NotFoundException;
import guru.springframework.spring5mongorecipeapp.services.RecipeServiceImpl;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    private static final String ID = "2";
    private static final String DESCRIPTION = "description";
    private static final Integer PREP_TIME = 10;
    private static final Integer COOK_TIME = 30;
    private static final Integer SERVINGS = 4;
    private static final String URL = "https://www.example.com";
    private static final String DIRECTIONS = "directions";

    @Mock
    RecipeServiceImpl recipeService;

    @Mock
    Model model;

    @InjectMocks
    RecipeController recipeController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                                 .setControllerAdvice(new ControllerExceptionHandler())
                                 .build();
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void showRecipe() throws Exception {
        // given
        Recipe recipe = new Recipe();
        recipe.setId("1");

        // when
        when(recipeService.findById("1")).thenReturn(Mono.just(recipe));

        // then
        mockMvc.perform(get("/recipes/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("recipes/show"))
               .andExpect(model().attributeExists("recipe"));
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void showRecipeNotFound() throws Exception {
        // when
        when(recipeService.findById("1")).thenThrow(NotFoundException.class);

        // then
        mockMvc.perform(get("/recipes/1"))
               .andExpect(view().name("404"))
               .andExpect(status().isNotFound());
    }

    @Disabled("Should test String ID format")
    @Test
    void showRecipeWrongIDFormat() throws Exception {
        // then
        mockMvc.perform(get("/recipes/abc"))
               .andExpect(view().name("400"))
               .andExpect(status().isBadRequest());
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void newRecipe() throws Exception {
        // then
        mockMvc.perform(get("/recipes/new"))
               .andExpect(status().isOk())
               .andExpect(view().name("recipes/form"))
               .andExpect(model().attributeExists("recipe"));
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void editRecipe() throws Exception {
        // given
        var command = new RecipeCommand();
        command.setId(ID);

        // when
        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

        // then
        mockMvc.perform(get("/recipes/2/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("recipes/form"))
               .andExpect(model().attributeExists("recipe"));
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void createOrUpdateRecipe() throws Exception {
        // given
        var command = new RecipeCommand();
        command.setId(ID);
        command.setPrepTime(PREP_TIME);
        command.setCookTime(COOK_TIME);
        command.setServings(SERVINGS);
        command.setUrl(URL);
        command.setDirections(DIRECTIONS);

        // when
        when(recipeService.saveCommand(any())).thenReturn(Mono.just(command));

        // then
        mockMvc.perform(
            post("/recipes").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("id", "")
                            .param("description", DESCRIPTION)
                            .param("prepTime", PREP_TIME.toString())
                            .param("cookTime", COOK_TIME.toString())
                            .param("servings", SERVINGS.toString())
                            .param("url", URL)
                            .param("directions", DIRECTIONS)
        ).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipes/2"));
    }

    @Disabled("App does not have validation right now.")
    @Test
    void createOrUpdateRecipeWithValidationFail() throws Exception {
        // then
        mockMvc.perform(
            post("/recipes").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("id", "")
                            .param("description", DESCRIPTION)
                            .param("prepTime", PREP_TIME.toString())
                            .param("cookTime", COOK_TIME.toString())
                            .param("servings", "200")
                            .param("url", URL)
                            .param("directions", DIRECTIONS)
        ).andExpect(status().isOk()).andExpect(view().name("recipes/form"));
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void destroyRecipe() throws Exception {
        // given
        when(recipeService.deleteById(anyString())).thenReturn(Mono.empty());

        // then
        mockMvc.perform(get("/recipes/1/delete"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/index"));

        verify(recipeService).deleteById(anyString());
    }

}
