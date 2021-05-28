package guru.springframework.spring5recipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5recipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;

class RecipeServiceImplTest {

    private static final Long ID = 1L;

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(
            recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand
        );
    }

    @Test
    void findById() {
        Recipe returnRecipe = new Recipe();
        returnRecipe.setId(ID);

        when(recipeRepository.findById(ID)).thenReturn(Optional.of(returnRecipe));

        Recipe recipe = recipeService.findById(ID);

        assertNotNull(recipe);
        assertEquals(ID, recipe.getId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void findByIdNotFound() {
        when(recipeRepository.findById(ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            recipeService.findById(ID);
        });

        assertEquals("Recipe with ID " + ID + " not found.", exception.getMessage());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void findCommandById() {
        var returnCommand = new RecipeCommand();
        returnCommand.setId(ID);
        Recipe returnRecipe = new Recipe();
        returnRecipe.setId(ID);

        when(recipeRepository.findById(ID)).thenReturn(Optional.of(returnRecipe));
        when(recipeToRecipeCommand.convert(any())).thenReturn(returnCommand);

        RecipeCommand command = recipeService.findCommandById(ID);

        assertNotNull(command);
        assertEquals(returnCommand.getId(), command.getId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void findAll() {
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.findAll();

        assertEquals(1, recipes.size());
        verify(recipeRepository).findAll();
        verify(recipeRepository, never()).findById(anyLong());
    }

    @Test
    void deleteById() {
        // given
        recipeService.deleteById(ID);

        // when

        // then
        verify(recipeRepository).deleteById(anyLong());
    }

}
