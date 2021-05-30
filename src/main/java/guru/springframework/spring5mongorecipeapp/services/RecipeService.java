package guru.springframework.spring5mongorecipeapp.services;

import guru.springframework.spring5mongorecipeapp.commands.RecipeCommand;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {

    Mono<Recipe> findById(String id);

    Mono<RecipeCommand> findCommandById(String id);

    Flux<Recipe> findAll();

    Mono<RecipeCommand> saveCommand(RecipeCommand command);

    Mono<Void> deleteById(String id);

}
