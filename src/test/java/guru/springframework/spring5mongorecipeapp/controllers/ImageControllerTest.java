package guru.springframework.spring5mongorecipeapp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.services.ImageService;
import guru.springframework.spring5mongorecipeapp.services.RecipeService;
import reactor.core.publisher.Mono;

class ImageControllerTest {

    private static final String COMMAND_ID = "1";
    private static final String IMAGE_TEXT = "imageText";
    private static final byte[] IMAGE_TEXT_BYTES = IMAGE_TEXT.getBytes();

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        imageController = new ImageController(imageService, recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                                 .setControllerAdvice(new ControllerExceptionHandler())
                                 .build();
    }

    @Test
    void show() throws Exception {
        // given
        var recipeCommand = new RecipeCommand();
        recipeCommand.setId(COMMAND_ID);

        var boxedBytes = new Byte[IMAGE_TEXT_BYTES.length];
        var i = 0;
        for (byte b : IMAGE_TEXT_BYTES) {
            boxedBytes[i++] = b;
        }

        recipeCommand.setImage(boxedBytes);

        // when
        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));
        MockHttpServletResponse response = mockMvc.perform(get("/recipes/1/image"))
                                                  .andExpect(status().isOk())
                                                  .andReturn()
                                                  .getResponse();
        byte[] responseBytes = response.getContentAsByteArray();

        // then
        assertEquals(IMAGE_TEXT_BYTES.length, responseBytes.length);
    }

    @Disabled
    @Test
    void showWrongIDFormat() throws Exception {
        // then
        mockMvc.perform(get("/recipes/abc/image"))
               .andExpect(view().name("400"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void edit() throws Exception {
        // given
        var recipeCommand = new RecipeCommand();
        recipeCommand.setId(COMMAND_ID);

        // when
        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));
        mockMvc.perform(get("/recipes/1/image/edit"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("recipe"));

        // then
        verify(recipeService).findCommandById(anyString());
    }

    @Test
    void upload() throws Exception {
        // given
        MockMultipartFile multipartFile = new MockMultipartFile(
            "imagefile", "testing.txt", "text/plain", "Spring Framework Guru".getBytes()
        );
        when(imageService.saveFile(anyString(), any(MultipartFile.class))).thenReturn(Mono.empty());

        // when
        mockMvc.perform(multipart("/recipes/1/image").file(multipartFile))
               .andExpect(status().is3xxRedirection())
               .andExpect(header().string("Location", "/recipes/1"));

        // then
        verify(imageService).saveFile(anyString(), any(MultipartFile.class));
    }

}
