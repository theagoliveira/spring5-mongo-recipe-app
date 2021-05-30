package guru.springframework.spring5mongorecipeapp.services;

import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Mono;

public interface ImageService {

    Mono<Void> saveFile(String recipeId, MultipartFile file);

}
