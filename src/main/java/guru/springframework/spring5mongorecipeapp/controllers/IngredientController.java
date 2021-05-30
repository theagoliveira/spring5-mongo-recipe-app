package guru.springframework.spring5mongorecipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.services.IngredientService;
import guru.springframework.spring5mongorecipeapp.services.RecipeService;
import guru.springframework.spring5mongorecipeapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/recipes/{recipeId}/ingredients")
public class IngredientController {

    private static final String INGREDIENTS_FORM = "recipes/ingredients/form";
    private static final String INGREDIENT_STR = "ingredient";
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    private WebDataBinder webDataBinder;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @InitBinder("ingredient")
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping({"", "/", "/index"})
    public String index(@PathVariable String recipeId, Model model) {
        log.debug("Get ingredients list for recipe with id " + recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipes/ingredients/index";
    }

    @GetMapping("/{id}")
    public String showIngredient(@PathVariable String id, @PathVariable String recipeId,
                                 Model model) {
        var ingredient = ingredientService.findCommandByIdAndRecipeId(id, recipeId).map(i -> {
            i.setRecipeId(recipeId);
            return i;
        });
        model.addAttribute(INGREDIENT_STR, ingredient);
        model.addAttribute("recipe", recipeService.findCommandById(recipeId));

        return "recipes/ingredients/show";
    }

    @GetMapping("/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        var ingredient = new IngredientCommand();

        if (recipeService.findCommandById(recipeId).share().block() == null) {
            // TODO: deal with error
            throw new RuntimeException("Recipe with ID " + recipeId + " does not exist.");
        }

        ingredient.setRecipeId(recipeId);
        ingredient.setUom(new UnitOfMeasureCommand());
        model.addAttribute(INGREDIENT_STR, ingredient);
        model.addAttribute("uoms", unitOfMeasureService.findAllCommands());

        return INGREDIENTS_FORM;
    }

    @GetMapping("/{id}/edit")
    public String editIngredient(@PathVariable String id, @PathVariable String recipeId,
                                 Model model) {
        var ingredient = ingredientService.findCommandByIdAndRecipeId(id, recipeId).map(i -> {
            i.setRecipeId(recipeId);
            return i;
        });
        model.addAttribute(INGREDIENT_STR, ingredient);
        model.addAttribute("uoms", unitOfMeasureService.findAllCommands());

        return INGREDIENTS_FORM;
    }

    @PostMapping
    public String createOrUpdateIngredient(@ModelAttribute("ingredient") IngredientCommand command,
                                           @PathVariable String recipeId, Model model) {
        log.info("Inside createOrUpdateIngredient.");

        webDataBinder.validate();
        var bindingResult = webDataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            model.addAttribute("uoms", unitOfMeasureService.findAllCommands());

            return INGREDIENTS_FORM;
        }

        command.setRecipeId(recipeId);
        var savedCommand = ingredientService.saveCommand(command);

        return "redirect:/recipes/" + recipeId + "/ingredients/"
                + savedCommand.map(IngredientCommand::getId).share().block();
    }

    @GetMapping("/{id}/delete")
    public String destroyIngredient(@PathVariable String id, @PathVariable String recipeId) {
        ingredientService.deleteByIdAndRecipeId(id, recipeId).block();

        return "redirect:/recipes/" + recipeId + "/ingredients";
    }

}
