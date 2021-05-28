package guru.springframework.spring5mongorecipeapp.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import guru.springframework.spring5mongorecipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;
import lombok.Synchronized;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure
        implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureCommand source) {
        if (source == null) {
            return null;
        }

        final var unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(source.getId());
        unitOfMeasure.setDescription(source.getDescription());
        return unitOfMeasure;
    }

}
