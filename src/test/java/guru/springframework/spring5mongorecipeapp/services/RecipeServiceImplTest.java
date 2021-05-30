package guru.springframework.spring5mongorecipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5mongorecipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.exceptions.NotFoundException;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class RecipeServiceImplTest {

    private static final String ID = "1";

    RecipeServiceImpl recipeService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(
            recipeReactiveRepository, recipeCommandToRecipe, recipeToRecipeCommand
        );
    }

    @Test
    void findById() {
        Recipe returnRecipe = new Recipe();
        returnRecipe.setId(ID);

        when(recipeReactiveRepository.findById(ID)).thenReturn(Mono.just(returnRecipe));

        Recipe recipe = recipeService.findById(ID).block();

        assertNotNull(recipe);
        assertEquals(ID, recipe.getId());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    void findByIdNotFound() {
        when(recipeReactiveRepository.findById(ID)).thenReturn(Mono.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            recipeService.findById(ID).block();
        });

        assertEquals("Recipe with ID " + ID + " not found.", exception.getMessage());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    void findCommandById() {
        var returnCommand = new RecipeCommand();
        returnCommand.setId(ID);
        Recipe returnRecipe = new Recipe();
        returnRecipe.setId(ID);

        when(recipeReactiveRepository.findById(ID)).thenReturn(Mono.just(returnRecipe));
        when(recipeToRecipeCommand.convert(any())).thenReturn(returnCommand);

        RecipeCommand command = recipeService.findCommandById(ID).block();

        assertNotNull(command);
        assertEquals(returnCommand.getId(), command.getId());
        verify(recipeReactiveRepository).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    void findAll() {
        Recipe recipe = new Recipe();

        when(recipeReactiveRepository.findAll()).thenReturn(Flux.just(recipe));

        Flux<Recipe> recipes = recipeService.findAll();

        assertEquals(1, recipes.count().block());
        verify(recipeReactiveRepository).findAll();
        verify(recipeReactiveRepository, never()).findById(anyString());
    }

    @Test
    void deleteById() {
        // given
        recipeService.deleteById(ID);

        // when

        // then
        verify(recipeReactiveRepository).deleteById(anyString());
    }

}
