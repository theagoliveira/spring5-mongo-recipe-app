package guru.springframework.spring5mongorecipeapp.repositories.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import guru.springframework.spring5mongorecipeapp.domain.Category;
import reactor.core.publisher.Mono;

public interface CategoryReactiveRepository extends ReactiveCrudRepository<Category, String> {

    Mono<Category> findByDescription(String description);

}
