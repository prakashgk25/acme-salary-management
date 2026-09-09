package com.acme.salary.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fx_rates", uniqueConstraints = @UniqueConstraint(columnNames = { "currency", "rate_date" }))
public class FxRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 3)
    private String currency;
    @Column(name = "rate_date", nullable = false)
    private LocalDate rateDate;
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal toUsd;

    protected FxRate() {
    }

    public FxRate(String currency, LocalDate date, BigDecimal toUsd) {
        this.currency = currency;
        this.rateDate = date;
        this.toUsd = toUsd;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getRateDate() {
        return rateDate;
    }

    public BigDecimal getToUsd() {
        return toUsd;
    }
}
