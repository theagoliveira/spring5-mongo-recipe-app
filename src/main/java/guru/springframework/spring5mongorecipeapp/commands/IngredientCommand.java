package guru.springframework.spring5mongorecipeapp.commands;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {

    private String id;
    private String recipeId;

    @NotBlank
    private String description;

    @Min(0)
    private BigDecimal amount;

    @NotNull
    private UnitOfMeasureCommand uom;

    public IngredientCommand(String id, String description, BigDecimal amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    @Override
    public String toString() {
        var df1 = new DecimalFormat("0");
        var df2 = new DecimalFormat("0.000");

        var amountIsNotNull = this.amount != null;
        var uomIsNotNull = this.uom != null;
        var uomDescriptionIsNotNull = uomIsNotNull && this.uom.getDescription() != null;
        var amountIs1 = amountIsNotNull && this.amount.compareTo(BigDecimal.valueOf(1)) == 0;
        var amountIsNot1 = amountIsNotNull && this.amount.compareTo(BigDecimal.valueOf(1)) != 0;
        var uomDescriptionIsDashOrPinch = uomDescriptionIsNotNull
                && (this.uom.getDescription().equals("dash")
                        || this.uom.getDescription().equals("pinch"));
        var uomDescriptionIsNotBlank = uomDescriptionIsNotNull
                && !this.uom.getDescription().equals("blank");
        var uomDescriptionEndsWithH = uomDescriptionIsNotNull
                && this.uom.getDescription().charAt(this.uom.getDescription().length() - 1) == 'h';

        var result = "";

        if (amountIsNotNull && !(uomDescriptionIsDashOrPinch && amountIs1)) {
            if (this.amount.doubleValue() % 1 == 0) {
                result += df1.format(this.amount);
            } else {
                switch (df2.format(this.amount)) {
                    case "0.500":
                        result += "½";
                        break;
                    case "0.250":
                        result += "¼";
                        break;
                    case "0.750":
                        result += "¾";
                        break;
                    case "0.125":
                        result += "⅛";
                        break;
                    default:
                        result += df2.format(this.amount);
                        break;
                }
            }
            result += " ";
        }

        if (uomIsNotNull && uomDescriptionIsNotBlank) {
            if (uomDescriptionIsNotNull) {
                if (uomDescriptionIsDashOrPinch && amountIs1) {
                    result += "a ";
                }

                result += this.uom.getDescription();

                if (amountIsNot1) {
                    if (uomDescriptionEndsWithH) {
                        result += "e";
                    }
                    result += "s";
                }
            }

            result += " of ";
        }

        result += this.description;

        return result;
    }

}
