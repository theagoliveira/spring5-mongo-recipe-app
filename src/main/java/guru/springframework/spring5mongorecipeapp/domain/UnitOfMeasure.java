package guru.springframework.spring5mongorecipeapp.domain;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class UnitOfMeasure {

    private String id = UUID.randomUUID().toString();
    private String description;

}
