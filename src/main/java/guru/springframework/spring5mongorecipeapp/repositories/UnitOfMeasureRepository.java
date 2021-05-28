package guru.springframework.spring5mongorecipeapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

    Optional<UnitOfMeasure> findByDescription(String description);

}
