package guru.springframework.spring5mongorecipeapp.services;

import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureService {

    Flux<UnitOfMeasureCommand> findAllCommands();

}
