/* package guru.springframework.spring5mongorecipeapp.controllers;

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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.services.IngredientService;
import guru.springframework.spring5mongorecipeapp.services.RecipeService;
import guru.springframework.spring5mongorecipeapp.services.UnitOfMeasureService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Disabled
class IngredientControllerTest {

    private static final String RECIPE_NAME = "name";
    private static final String ID = "1";
    private static final String DESCRIPTION = "description";

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    IngredientController ingredientController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientController = new IngredientController(
            recipeService, ingredientService, unitOfMeasureService
        );
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void index() throws Exception {
        // given
        var command = new RecipeCommand();

        // when
        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));
        mockMvc.perform(get("/recipes/1/ingredients"))
               .andExpect(status().isOk())
               .andExpect(view().name("recipes/ingredients/index"))
               .andExpect(model().attributeExists("recipe"));

        // then
        verify(recipeService).findCommandById(anyString());
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void showIngredient() throws Exception {
        // given
        var ingredientCommand = new IngredientCommand();
        var recipeCommand = new RecipeCommand();
        recipeCommand.setName(RECIPE_NAME);

        // when
        when(ingredientService.findCommandByIdAndRecipeId(anyString(), anyString())).thenReturn(
            Mono.just(ingredientCommand)
        );
        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));
        mockMvc.perform(get("/recipes/1/ingredients/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("recipes/ingredients/show"))
               .andExpect(model().attributeExists("ingredient"))
               .andExpect(model().attributeExists("recipe"));

        // then
        verify(ingredientService).findCommandByIdAndRecipeId(anyString(), anyString());
        verify(recipeService).findCommandById(anyString());
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void newIngredient() throws Exception {
        // when
        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(new RecipeCommand()));
        when(unitOfMeasureService.findAllCommands()).thenReturn(
            Flux.just(new UnitOfMeasureCommand())
        );
        mockMvc.perform(get("/recipes/1/ingredients/new"))
               .andExpect(status().isOk())
               .andExpect(view().name("recipes/ingredients/form"))
               .andExpect(model().attributeExists("ingredient"))
               .andExpect(model().attributeExists("uoms"));

        // then
        verify(unitOfMeasureService).findAllCommands();
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void editIngredient() throws Exception {
        // given
        var ingredientCommand = new IngredientCommand();

        // when
        when(ingredientService.findCommandByIdAndRecipeId(anyString(), anyString())).thenReturn(
            Mono.just(ingredientCommand)
        );
        when(unitOfMeasureService.findAllCommands()).thenReturn(
            Flux.just(new UnitOfMeasureCommand())
        );
        mockMvc.perform(get("/recipes/1/ingredients/1/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("recipes/ingredients/form"))
               .andExpect(model().attributeExists("ingredient"))
               .andExpect(model().attributeExists("uoms"));

        // then
        verify(ingredientService).findCommandByIdAndRecipeId(anyString(), anyString());
        verify(unitOfMeasureService).findAllCommands();
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void createOrUpdateIngredient() throws Exception {
        // given
        var command = new IngredientCommand();
        command.setId(ID);

        // when
        when(ingredientService.saveCommand(any())).thenReturn(Mono.just(command));

        // then
        mockMvc.perform(
            post("/recipes/1/ingredients").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                          .param("id", "")
                                          .param("description", DESCRIPTION)
        )
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/recipes/1/ingredients/1"));
    }

    @Disabled("Needs to be refactored for WebFlux")
    @Test
    void destroyIngredient() throws Exception {
        when(ingredientService.deleteByIdAndRecipeId(anyString(), anyString())).thenReturn(
            Mono.empty()
        );

        // then
        mockMvc.perform(get("/recipes/1/ingredients/1/delete"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/recipes/1/ingredients"));

        verify(ingredientService).deleteByIdAndRecipeId(anyString(), anyString());
    }

}
 */