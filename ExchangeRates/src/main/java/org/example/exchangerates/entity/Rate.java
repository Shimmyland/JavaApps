package org.example.exchangerates.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    private Cur baseCurrency;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Cur currency;
    private double price;

    public Rate(LocalDate fromDate, Cur baseCurrency, Cur currency, double price) {
        this.fromDate = fromDate;
        this.savedAt = LocalDateTime.now();
        this.baseCurrency = baseCurrency;
        this.currency = currency;
        this.price = price;
    }
}
