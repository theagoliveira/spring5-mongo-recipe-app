package guru.springframework.spring5recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveFile(Long recipeId, MultipartFile file);

}
