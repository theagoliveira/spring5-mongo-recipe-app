package guru.springframework.spring5mongorecipeapp.repositories.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import guru.springframework.spring5mongorecipeapp.domain.Recipe;

public interface RecipeReactiveRepository extends ReactiveCrudRepository<Recipe, String> {}
