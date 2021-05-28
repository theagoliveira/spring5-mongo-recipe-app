package guru.springframework.spring5mongorecipeapp.repositories;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.spring5mongorecipeapp.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

}
