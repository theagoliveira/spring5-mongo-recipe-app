package guru.springframework.spring5mongorecipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.spring5mongorecipeapp.commands.IngredientCommand;
import guru.springframework.spring5mongorecipeapp.domain.Ingredient;
import lombok.Synchronized;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    public IngredientCommandToIngredient(UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure) {
        this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
    }

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {
        if (source == null) {
            return null;
        }

        final var ingredient = new Ingredient();
        ingredient.setId(source.getId());
        ingredient.setDescription(source.getDescription());
        ingredient.setAmount(source.getAmount());
        ingredient.setUom(unitOfMeasureCommandToUnitOfMeasure.convert(source.getUom()));
        return ingredient;
    }

}
