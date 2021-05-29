package guru.springframework.spring5mongorecipeapp.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import guru.springframework.spring5mongorecipeapp.bootstrap.DataLoader;
import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.CategoryReactiveRepository;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.RecipeReactiveRepository;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;

@DataMongoTest
class UnitOfMeasureRepositoryIT {

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
        recipeRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();
        categoryRepository.deleteAll();

        var dataLoader = new DataLoader(
            recipeRepository, unitOfMeasureRepository, categoryRepository, recipeReactiveRepository,
            unitOfMeasureReactiveRepository, categoryReactiveRepository
        );

        dataLoader.run();
    }

    @Test
    void findByDescriptionTeaspoon() {
        Optional<UnitOfMeasure> optionalUom = unitOfMeasureRepository.findByDescription("teaspoon");

        assertEquals("teaspoon", optionalUom.get().getDescription());
    }

    @Test
    void findByDescriptionCup() {
        Optional<UnitOfMeasure> optionalUom = unitOfMeasureRepository.findByDescription("cup");

        assertEquals("cup", optionalUom.get().getDescription());
    }

}
