package org.example.exchangerates.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")   // před savem do DB je id null!, https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/
@ToString(of = {"id", "code"})
@Table(name = "currency")
public class Cur {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    private String code;
    private String name;
    private String symbol;
    private String type;

    @OneToMany(mappedBy = "baseCur")
    private List<Rate> baseCurrencyRates;
    @OneToMany(mappedBy = "cur")
    private List<Rate> currencyRates;


    public Cur(String code, String name, String symbol, String type) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.type = type;
    }
}
