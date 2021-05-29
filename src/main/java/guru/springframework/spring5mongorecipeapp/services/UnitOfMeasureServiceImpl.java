package guru.springframework.spring5mongorecipeapp.services;

import org.springframework.stereotype.Service;

import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                                    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    public Flux<UnitOfMeasureCommand> findAllCommands() {
        return unitOfMeasureReactiveRepository.findAll()
                                              .map(unitOfMeasureToUnitOfMeasureCommand::convert);
    }

}
