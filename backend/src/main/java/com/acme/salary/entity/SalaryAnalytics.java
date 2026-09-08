package com.acme.salary.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_analytics")
public class SalaryAnalytics {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long totalEmployees;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPayrollUsd;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal averageSalaryUsd;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minimumSalaryUsd;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal maximumSalaryUsd;

    @Column(nullable = false)
    private LocalDateTime calculatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(Long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public BigDecimal getTotalPayrollUsd() {
        return totalPayrollUsd;
    }

    public void setTotalPayrollUsd(BigDecimal totalPayrollUsd) {
        this.totalPayrollUsd = totalPayrollUsd;
    }

    public BigDecimal getAverageSalaryUsd() {
        return averageSalaryUsd;
    }

    public void setAverageSalaryUsd(BigDecimal averageSalaryUsd) {
        this.averageSalaryUsd = averageSalaryUsd;
    }

    public BigDecimal getMinimumSalaryUsd() {
        return minimumSalaryUsd;
    }

    public void setMinimumSalaryUsd(BigDecimal minimumSalaryUsd) {
        this.minimumSalaryUsd = minimumSalaryUsd;
    }

    public BigDecimal getMaximumSalaryUsd() {
        return maximumSalaryUsd;
    }

    public void setMaximumSalaryUsd(BigDecimal maximumSalaryUsd) {
        this.maximumSalaryUsd = maximumSalaryUsd;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}