package com.acme.salary.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record EmployeeRequest(@NotBlank String employeeNumber,@NotBlank String firstName,@NotBlank String lastName,@NotBlank String department,@NotBlank String country,@NotBlank @Size(min=3,max=3) String currency,@NotNull @DecimalMin("0.01") BigDecimal annualSalary,@NotNull LocalDate effectiveDate,@NotNull Long version) {}
