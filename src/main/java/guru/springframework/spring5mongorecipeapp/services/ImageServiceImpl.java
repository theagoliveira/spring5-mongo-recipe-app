package guru.springframework.spring5mongorecipeapp.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveFile(String recipeId, MultipartFile file) {
        Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId).map(recipe -> {
            try {
                var fileBytes = file.getBytes();
                var boxedFileBytes = new Byte[fileBytes.length];
                var i = 0;
                for (byte b : fileBytes) {
                    boxedFileBytes[i++] = b;
                }

                recipe.setImage(boxedFileBytes);
                return recipe;
            } catch (IOException e) {
                log.error("IO Exception occurred.", e);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        recipeReactiveRepository.save(recipeMono.block()).block();

        return Mono.empty();
    }

}
