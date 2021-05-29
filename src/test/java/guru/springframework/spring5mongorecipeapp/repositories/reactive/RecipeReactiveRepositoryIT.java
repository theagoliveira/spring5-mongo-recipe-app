package guru.springframework.spring5mongorecipeapp.repositories.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import guru.springframework.spring5mongorecipeapp.bootstrap.DataLoader;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.repositories.CategoryRepository;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import guru.springframework.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;
import reactor.core.publisher.Flux;

@DataMongoTest
class RecipeReactiveRepositoryIT {

    private static final int RECIPE_COUNT = 2;
    private static final String RECIPE_ID = "1";
    private static final String RECIPE_DESCRIPTION = "description";

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @BeforeEach
    void setUp() throws Exception {
        recipeReactiveRepository.deleteAll().block();
        unitOfMeasureReactiveRepository.deleteAll().block();
        categoryReactiveRepository.deleteAll().block();

        var dataLoader = new DataLoader(
            recipeRepository, unitOfMeasureRepository, categoryRepository, recipeReactiveRepository,
            unitOfMeasureReactiveRepository, categoryReactiveRepository
        );

        dataLoader.run();
    }

    @Test
    void saveWithID() {
        Recipe savedUom;
        Flux<Recipe> fluxUom;

        var uom1 = new Recipe();
        uom1.setId(RECIPE_ID);
        uom1.setDescription(RECIPE_DESCRIPTION);

        savedUom = recipeReactiveRepository.save(uom1).block();
        fluxUom = recipeReactiveRepository.findAll();

        assertEquals(RECIPE_ID, savedUom.getId());
        assertEquals(RECIPE_DESCRIPTION, savedUom.getDescription());
        assertEquals(RECIPE_COUNT + 1, fluxUom.count().block());
    }

    @Test
    void saveWithoutID() {
        Recipe savedUom;
        Flux<Recipe> fluxUom;

        var uom1 = new Recipe();
        uom1.setDescription(RECIPE_DESCRIPTION);

        savedUom = recipeReactiveRepository.save(uom1).block();
        fluxUom = recipeReactiveRepository.findAll();

        assertNotNull(savedUom.getId());
        assertEquals(RECIPE_DESCRIPTION, savedUom.getDescription());
        assertEquals(RECIPE_COUNT + 1, fluxUom.count().block());
    }

    @Test
    void findAll() {
        Flux<Recipe> fluxUom;

        fluxUom = recipeReactiveRepository.findAll();

        assertEquals(RECIPE_COUNT, fluxUom.count().block());
    }

}
