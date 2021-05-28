package guru.springframework.spring5mongorecipeapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.spring5mongorecipeapp.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {

    Optional<Category> findByDescription(String description);

}
