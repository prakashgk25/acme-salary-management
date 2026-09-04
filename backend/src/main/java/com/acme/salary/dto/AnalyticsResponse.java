package com.acme.salary.dto;
import java.math.BigDecimal;
public record AnalyticsResponse(long employeeCount,BigDecimal averageUsdSalary,BigDecimal totalUsdPayroll) {}
