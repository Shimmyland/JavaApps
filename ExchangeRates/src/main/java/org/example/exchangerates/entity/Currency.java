package org.example.exchangerates.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder    // https://www.baeldung.com/lombok-builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
// before save into DB is id null, https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/
@ToString(of = {"id", "code"})
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String code;
    private String name;
    private String symbol;
    private String type;

    @OneToMany(mappedBy = "baseCurrency")
    private List<Rate> baseCurrencyRates;
    @OneToMany(mappedBy = "currency")
    private List<Rate> currencyRates;
}
