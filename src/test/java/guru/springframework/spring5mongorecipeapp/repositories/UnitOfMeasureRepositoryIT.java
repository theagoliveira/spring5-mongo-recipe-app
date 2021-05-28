package guru.springframework.spring5recipeapp.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring5recipeapp.domain.UnitOfMeasure;

@DataJpaTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {

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
