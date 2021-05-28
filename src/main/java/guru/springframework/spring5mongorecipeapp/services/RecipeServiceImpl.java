package guru.springframework.spring5mongorecipeapp.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.converters.RecipeCommandToRecipe;
import guru.springframework.spring5mongorecipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.exceptions.NotFoundException;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Recipe findById(String id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isPresent()) {
            return optionalRecipe.get();
        }

        throw new NotFoundException("Recipe with ID " + id + " not found.");
    }

    @Override
    public RecipeCommand findCommandById(String id) {
        return recipeToRecipeCommand.convert(this.findById(id));
    }

    @Override
    public Set<Recipe> findAll() {
        log.info("Inside findAll() method of RecipeService");

        Set<Recipe> recipeSet = new HashSet<>();

        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);

        return recipeSet;
    }

    @Override
    public RecipeCommand saveCommand(RecipeCommand command) {
        var detachedRecipe = recipeCommandToRecipe.convert(command);

        if (detachedRecipe != null) {
            var savedRecipe = recipeRepository.save(detachedRecipe);
            log.debug("Saved recipe with ID " + savedRecipe.getId());
            return recipeToRecipeCommand.convert(savedRecipe);
        } else {
            log.debug("Recipe not saved because command is null");
            return null;
        }
    }

    @Override
    public void deleteById(String id) {
        recipeRepository.deleteById(id);
    }

}
