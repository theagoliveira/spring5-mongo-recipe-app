package guru.springframework.spring5mongorecipeapp.repositories.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository
        extends ReactiveCrudRepository<UnitOfMeasure, String> {

    Mono<UnitOfMeasure> findByDescription(String description);

}
