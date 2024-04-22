package org.example.exchangerates.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Table(name = "rate")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;

    @ManyToOne
    @JoinColumn (name = "base_currency_id")
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private double price;

    public Rate(LocalDate fromDate, Currency baseCurrency, Currency currency, double price) {
        this.fromDate = fromDate;
        this.savedAt = LocalDateTime.now();
        this.baseCurrency = baseCurrency;
        this.currency = currency;
        this.price = price;
    }
}
