package guru.springframework.spring5mongorecipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;

class ImageServiceImplTest {

    private static final String ID = "1";

    @Mock
    RecipeRepository recipeRepository;

    ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void saveFile() throws Exception {
        // given
        var multipartFile = new MockMultipartFile(
            "imagefile", "testing.txt", "text/plain", "Spring Framework Guru".getBytes()
        );
        var recipe = new Recipe();
        recipe.setId(ID);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        // when
        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);
        imageService.saveFile(ID, multipartFile);

        // then
        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

}
