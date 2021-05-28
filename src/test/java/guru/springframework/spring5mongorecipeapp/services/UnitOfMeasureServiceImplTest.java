package guru.springframework.spring5recipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;

public class UnitOfMeasureServiceImplTest {

    private static final Long UOM_ID1 = 1L;
    private static final Long UOM_ID2 = 2L;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    UnitOfMeasureService unitOfMeasureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(
            unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand
        );
    }

    @Test
    void findAllCommands() {
        // given
        var uoms = new HashSet<UnitOfMeasure>();

        var uom1 = new UnitOfMeasure();
        uom1.setId(UOM_ID1);
        var uom2 = new UnitOfMeasure();
        uom2.setId(UOM_ID2);

        uoms.add(uom1);
        uoms.add(uom2);

        // when
        when(unitOfMeasureRepository.findAll()).thenReturn(uoms);
        Set<UnitOfMeasureCommand> commands = unitOfMeasureService.findAllCommands();

        // then
        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository).findAll();
    }

}
