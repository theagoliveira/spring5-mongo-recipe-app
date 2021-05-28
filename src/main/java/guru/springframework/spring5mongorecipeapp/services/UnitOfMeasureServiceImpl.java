package guru.springframework.spring5mongorecipeapp.services;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    UnitOfMeasureRepository unitOfMeasureRepository;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository,
                                    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    public Set<UnitOfMeasureCommand> findAllCommands() {
        return StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(), false)
                            .map(unitOfMeasureToUnitOfMeasureCommand::convert)
                            .collect(Collectors.toSet());
    }

}
