package guru.springframework.spring5recipeapp.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveFile(Long recipeId, MultipartFile file) {
        var optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isPresent()) {
            try {
                var recipe = optionalRecipe.get();
                var fileBytes = file.getBytes();
                var boxedFileBytes = new Byte[fileBytes.length];
                var i = 0;
                for (byte b : fileBytes) {
                    boxedFileBytes[i++] = b;
                }

                recipe.setImage(boxedFileBytes);
                recipeRepository.save(recipe);
            } catch (IOException e) {
                log.error("IO Exception occurred.", e);
                e.printStackTrace();
            }
        } else {
            log.error("Recipe with ID " + recipeId + " not found.");
        }
    }

}
