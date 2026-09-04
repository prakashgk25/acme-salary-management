package com.acme.salary.dto;
import java.math.BigDecimal; import java.time.LocalDate;
public record EmployeeResponse(Long id,Long version,String employeeNumber,String firstName,String lastName,String department,String country,String currency,BigDecimal annualSalary,BigDecimal normalizedUsdSalary,LocalDate effectiveDate) {}
