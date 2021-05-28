package guru.springframework.spring5mongorecipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.spring5mongorecipeapp.services.RecipeServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {

    private final RecipeServiceImpl recipeService;

    public IndexController(RecipeServiceImpl recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        log.info("Index page requested.");

        model.addAttribute("recipes", recipeService.findAll());
        log.debug("Recipes added to model. Returning.");

        return "index";
    }

}
