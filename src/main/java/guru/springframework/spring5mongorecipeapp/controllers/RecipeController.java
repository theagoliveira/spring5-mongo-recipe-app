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

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private static final String RECIPES_FORM = "recipes/form";
    private static final String RECIPE_STR = "recipe";
    private final RecipeService recipeService;

    private WebDataBinder webDataBinder;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/{id}")
    public String showRecipe(@PathVariable String id, Model model) {
        var recipe = recipeService.findById(id);
        model.addAttribute(RECIPE_STR, recipe);

        return "recipes/show";
    }

    @GetMapping("/new")
    public String newRecipe(Model model) {
        var recipe = new RecipeCommand();
        model.addAttribute(RECIPE_STR, recipe);

        return RECIPES_FORM;
    }

    @GetMapping("/{id}/edit")
    public String editRecipe(@PathVariable String id, Model model) {
        var recipe = recipeService.findCommandById(id);
        model.addAttribute(RECIPE_STR, recipe);

        return RECIPES_FORM;
    }

    @PostMapping
    public String createOrUpdateRecipe(@ModelAttribute("recipe") RecipeCommand command) {
        log.info("Inside createOrUpdateRecipe.");

        webDataBinder.validate();
        var bindingResult = webDataBinder.getBindingResult();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));

            return RECIPES_FORM;
        }

        var savedCommand = recipeService.saveCommand(command);

        return "redirect:/recipes/" + savedCommand.map(RecipeCommand::getId).share().block();
    }

    @GetMapping("/{id}/delete")
    public String destroyRecipe(@PathVariable String id) {
        recipeService.deleteById(id).share().block();

        return "redirect:/index";
    }

    // @ResponseStatus(HttpStatus.NOT_FOUND)
    // @ExceptionHandler(NotFoundException.class)
    // public ModelAndView handleNotFound(Exception exception) {
    //     log.error("Handling not found exception.");
    //     log.error("Message: " + exception.getMessage());

    //     var modelAndView = new ModelAndView();
    //     modelAndView.setViewName("404");
    //     modelAndView.addObject("exception", exception);

    //     return modelAndView;
    // }

}
