package org.example.exchangerates.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "fromDate"})
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
    @JoinColumn(name = "base_currency_id")
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private double price;
}
