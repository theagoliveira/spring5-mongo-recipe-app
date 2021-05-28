package guru.springframework.spring5recipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.services.IngredientService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import guru.springframework.spring5recipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/recipes/{recipeId}/ingredients")
public class IngredientController {

    private static final String INGREDIENT_STR = "ingredient";
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping({"", "/", "/index"})
    public String index(@PathVariable Long recipeId, Model model) {
        log.debug("Get ingredients list for recipe with id " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipes/ingredients/index";
    }

    @GetMapping("/{id}")
    public String showIngredient(@PathVariable Long id, @PathVariable Long recipeId, Model model) {
        var ingredient = ingredientService.findCommandByIdAndRecipeId(id, recipeId);
        model.addAttribute(INGREDIENT_STR, ingredient);
        model.addAttribute("recipeName", recipeService.findCommandById(recipeId).getName());

        return "recipes/ingredients/show";
    }

    @GetMapping("/new")
    public String newIngredient(@PathVariable Long recipeId, Model model) {
        var ingredient = new IngredientCommand();

        if (recipeService.findCommandById(recipeId) == null) {
            // TODO: deal with error
            throw new RuntimeException("Recipe with ID " + recipeId + " does not exist.");
        }

        ingredient.setRecipeId(recipeId);
        ingredient.setUom(new UnitOfMeasureCommand());
        model.addAttribute(INGREDIENT_STR, ingredient);
        model.addAttribute("uoms", unitOfMeasureService.findAllCommands());

        return "recipes/ingredients/form";
    }

    @GetMapping("/{id}/edit")
    public String editIngredient(@PathVariable Long id, @PathVariable Long recipeId, Model model) {
        var ingredient = ingredientService.findCommandByIdAndRecipeId(id, recipeId);
        model.addAttribute(INGREDIENT_STR, ingredient);
        model.addAttribute("uoms", unitOfMeasureService.findAllCommands());

        return "recipes/ingredients/form";
    }

    @PostMapping
    public String createOrUpdateIngredient(@ModelAttribute IngredientCommand command,
                                           @PathVariable Long recipeId) {
        IngredientCommand savedCommand = ingredientService.saveCommand(command);

        return "redirect:/recipes/" + recipeId + "/ingredients/" + savedCommand.getId();
    }

    @GetMapping("/{id}/delete")
    public String destroyIngredient(@PathVariable Long id, @PathVariable Long recipeId) {
        ingredientService.deleteByIdAndRecipeId(id, recipeId);

        return "redirect:/recipes/" + recipeId + "/ingredients";
    }

}
