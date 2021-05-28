package guru.springframework.spring5mongorecipeapp.services;

import java.util.Set;

import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> findAllCommands();

}
