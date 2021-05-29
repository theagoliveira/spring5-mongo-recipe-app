package guru.springframework.spring5mongorecipeapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Flux;

public class UnitOfMeasureServiceImplTest {

    private static final String UOM_ID1 = "1";
    private static final String UOM_ID2 = "2";

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    UnitOfMeasureService unitOfMeasureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(
            unitOfMeasureReactiveRepository, unitOfMeasureToUnitOfMeasureCommand
        );
    }

    @Test
    void findAllCommands() {
        // given
        var uom1 = new UnitOfMeasure();
        uom1.setId(UOM_ID1);
        var uom2 = new UnitOfMeasure();
        uom2.setId(UOM_ID2);
        when(unitOfMeasureReactiveRepository.findAll()).thenReturn(Flux.just(uom1, uom2));

        // when
        List<UnitOfMeasureCommand> commands = unitOfMeasureService.findAllCommands()
                                                                  .collectList()
                                                                  .block();

        // then
        assertEquals(2, commands.size());
        verify(unitOfMeasureReactiveRepository).findAll();
    }

}
