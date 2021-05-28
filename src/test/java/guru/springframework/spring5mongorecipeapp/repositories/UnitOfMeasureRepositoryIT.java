package guru.springframework.spring5mongorecipeapp.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;

@Disabled
@SpringBootTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {

    }

    @Disabled
    @Test
    void findByDescriptionTeaspoon() {
        Optional<UnitOfMeasure> optionalUom = unitOfMeasureRepository.findByDescription("teaspoon");

        assertEquals("teaspoon", optionalUom.get().getDescription());
    }

    @Disabled
    @Test
    void findByDescriptionCup() {
        Optional<UnitOfMeasure> optionalUom = unitOfMeasureRepository.findByDescription("cup");

        assertEquals("cup", optionalUom.get().getDescription());
    }

}
