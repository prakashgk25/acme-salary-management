package com.acme.salary.repository;

import java.math.BigDecimal;

public interface SalaryAnalyticsProjection {

    Long getTotalEmployees();

    BigDecimal getTotalPayrollUsd();

    BigDecimal getAverageSalaryUsd();

    BigDecimal getMinimumSalaryUsd();

    BigDecimal getMaximumSalaryUsd();
}