package org.example.exchangerates.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "currency")
public class Cur {
    // shortcut for Currency, it is reserved word

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    private String code;
    private String name;
    private String symbol;
    private String type;


    @OneToMany(mappedBy = "baseCurrency")
    private List<Rate> baseCurrencyRates;
    @OneToMany(mappedBy = "currency")
    private List<Rate> currencyRates;


    public Cur(String code, String name, String symbol, String type) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.type = type;
    }
}
