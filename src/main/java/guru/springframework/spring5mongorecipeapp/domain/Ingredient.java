package guru.springframework.spring5recipeapp.domain;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "amount", precision = 6, scale = 3)
    private BigDecimal amount;

    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure uom;

    @ManyToOne
    private Recipe recipe;

    public Ingredient(Long id, String description, BigDecimal amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    @Override
    public String toString() {
        var df1 = new DecimalFormat("0");
        var df2 = new DecimalFormat("0.000");
        var amountIs1 = (amount != null) && (amount.compareTo(BigDecimal.valueOf(1)) == 0);
        var result = "";

        if (amount != null && !((uom != null) && (uom.getDescription() != null)
                && (uom.getDescription().equals("dash") || uom.getDescription().equals("pinch"))
                && amountIs1)) {
            if (amount.doubleValue() % 1 == 0) {
                result += df1.format(amount);
            } else {
                switch (df2.format(amount)) {
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
                        result += df2.format(amount);
                        break;
                }
            }
            result += " ";
        }

        if (uom != null) {
            if (uom.getDescription() != null) {
                if ((uom.getDescription().equals("dash") || uom.getDescription().equals("pinch"))
                        && amountIs1) {
                    result += "a ";
                }

                result += uom.getDescription();
            }

            if (amount != null && amount.compareTo(BigDecimal.valueOf(1)) != 0) {
                if (uom.getDescription().charAt(uom.getDescription().length() - 1) == 'h') {
                    result += "e";
                }
                result += "s";
            }

            result += " of ";
        }

        result += description;

        return result;
    }

}
