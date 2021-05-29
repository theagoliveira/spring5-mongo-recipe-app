package guru.springframework.spring5mongorecipeapp.repositories.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import guru.springframework.spring5mongorecipeapp.bootstrap.DataLoader;
import guru.springframework.spring5mongorecipeapp.domain.Category;
import guru.springframework.spring5mongorecipeapp.repositories.CategoryRepository;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import guru.springframework.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@DataMongoTest
class CategoryReactiveRepositoryIT {

    private static final String MEXICAN = "mexican";
    private static final int CATEGORY_COUNT = 4;
    private static final String CATEGORY_ID = "1";
    private static final String CATEGORY_DESCRIPTION = "description";

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
    void findByDescription() {
        Mono<Category> monoUom = categoryReactiveRepository.findByDescription(MEXICAN);

        assertEquals(MEXICAN, monoUom.block().getDescription());
    }

    @Test
    void saveWithID() {
        Category savedUom;
        Flux<Category> fluxUom;

        var uom1 = new Category();
        uom1.setId(CATEGORY_ID);
        uom1.setDescription(CATEGORY_DESCRIPTION);

        savedUom = categoryReactiveRepository.save(uom1).block();
        fluxUom = categoryReactiveRepository.findAll();

        assertEquals(CATEGORY_ID, savedUom.getId());
        assertEquals(CATEGORY_DESCRIPTION, savedUom.getDescription());
        assertEquals(CATEGORY_COUNT + 1, fluxUom.count().block());
    }

    @Test
    void saveWithoutID() {
        Category savedUom;
        Flux<Category> fluxUom;

        var uom1 = new Category();
        uom1.setDescription(CATEGORY_DESCRIPTION);

        savedUom = categoryReactiveRepository.save(uom1).block();
        fluxUom = categoryReactiveRepository.findAll();

        assertNotNull(savedUom.getId());
        assertEquals(CATEGORY_DESCRIPTION, savedUom.getDescription());
        assertEquals(CATEGORY_COUNT + 1, fluxUom.count().block());
    }

    @Test
    void findAll() {
        Flux<Category> fluxUom;

        fluxUom = categoryReactiveRepository.findAll();

        assertEquals(CATEGORY_COUNT, fluxUom.count().block());
    }

}
