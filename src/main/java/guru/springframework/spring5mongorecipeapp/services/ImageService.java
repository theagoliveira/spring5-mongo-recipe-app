package guru.springframework.spring5mongorecipeapp.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveFile(String recipeId, MultipartFile file);

}
