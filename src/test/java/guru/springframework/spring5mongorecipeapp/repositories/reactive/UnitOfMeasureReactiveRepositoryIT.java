package guru.springframework.spring5mongorecipeapp.repositories.reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import guru.springframework.spring5mongorecipeapp.bootstrap.DataLoader;
import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5mongorecipeapp.repositories.CategoryRepository;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import guru.springframework.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@DataMongoTest
class UnitOfMeasureReactiveRepositoryIT {

    private static final String TEASPOON = "teaspoon";
    private static final int UOM_COUNT = 7;
    private static final String UOM_ID = "1";
    private static final String UOM_DESCRIPTION = "description";

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
        Mono<UnitOfMeasure> monoUom = unitOfMeasureReactiveRepository.findByDescription(TEASPOON);

        assertEquals(TEASPOON, monoUom.block().getDescription());
    }

    @Test
    void saveWithID() {
        UnitOfMeasure savedUom;
        Flux<UnitOfMeasure> fluxUom;

        var uom1 = new UnitOfMeasure();
        uom1.setId(UOM_ID);
        uom1.setDescription(UOM_DESCRIPTION);

        savedUom = unitOfMeasureReactiveRepository.save(uom1).block();
        fluxUom = unitOfMeasureReactiveRepository.findAll();

        assertEquals(UOM_ID, savedUom.getId());
        assertEquals(UOM_DESCRIPTION, savedUom.getDescription());
        assertEquals(UOM_COUNT + 1, fluxUom.count().block());
    }

    @Test
    void saveWithoutID() {
        UnitOfMeasure savedUom;
        Flux<UnitOfMeasure> fluxUom;

        var uom1 = new UnitOfMeasure();
        uom1.setDescription(UOM_DESCRIPTION);

        savedUom = unitOfMeasureReactiveRepository.save(uom1).block();
        fluxUom = unitOfMeasureReactiveRepository.findAll();

        assertNotNull(savedUom.getId());
        assertEquals(UOM_DESCRIPTION, savedUom.getDescription());
        assertEquals(UOM_COUNT + 1, fluxUom.count().block());
    }

    @Test
    void findAll() {
        Flux<UnitOfMeasure> fluxUom;

        fluxUom = unitOfMeasureReactiveRepository.findAll();

        assertEquals(UOM_COUNT, fluxUom.count().block());
    }

}
