package guru.springframework.spring5mongorecipeapp.controllers;

// import java.io.ByteArrayInputStream;
// import java.io.IOException;
// import java.io.InputStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.spring5mongorecipeapp.services.ImageService;
import guru.springframework.spring5mongorecipeapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/recipes/{recipeId}/image")
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    // @GetMapping
    // public void show(@PathVariable String recipeId,
    //                  HttpServletResponse response) throws IOException {
    //     var recipeCommand = recipeService.findCommandById(recipeId);
    //     var recipeImage = recipeCommand.block().getImage();
    //     var unboxedBytes = new byte[recipeImage.length];
    //     var i = 0;

    //     for (Byte b : recipeImage) {
    //         unboxedBytes[i++] = b;
    //     }

    //     response.setContentType("image/jpeg");
    //     InputStream is = new ByteArrayInputStream(unboxedBytes);
    //     IOUtils.copy(is, response.getOutputStream());
    // }

    @GetMapping("/edit")
    public String edit(@PathVariable String recipeId, Model model) {
        var recipeCommand = recipeService.findCommandById(recipeId);
        model.addAttribute("recipe", recipeCommand.block());

        return "recipes/image/form";
    }

    @PostMapping
    public String upload(@PathVariable String recipeId,
                         @RequestParam("imagefile") MultipartFile file) {
        log.info("Received a file.");
        imageService.saveFile(recipeId, file).block();

        return "redirect:/recipes/" + recipeId;
    }

}
