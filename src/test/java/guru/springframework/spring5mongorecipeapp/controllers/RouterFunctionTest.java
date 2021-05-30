package guru.springframework.spring5mongorecipeapp.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import guru.springframework.spring5mongorecipeapp.config.WebConfig;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.services.RecipeService;
import reactor.core.publisher.Flux;

class RouterFunctionTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        var webConfig = new WebConfig();
        RouterFunction<?> routerFunction = webConfig.routes(recipeService);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void findAllWithoutData() {
        when(recipeService.findAll()).thenReturn(Flux.empty());

        webTestClient.get()
                     .uri("/api/recipes")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();

    }

    @Test
    void findAllWithData() {
        when(recipeService.findAll()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        webTestClient.get()
                     .uri("/api/recipes")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBodyList(Recipe.class);

    }

}
