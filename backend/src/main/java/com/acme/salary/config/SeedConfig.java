package com.acme.salary.config;

import com.acme.salary.entity.Employee;
import com.acme.salary.entity.FxRate;
import com.acme.salary.repository.EmployeeRepository;
import com.acme.salary.repository.FxRateRepository;
import com.acme.salary.repository.SalaryAnalyticsRepository;
import com.acme.salary.service.AnalyticsService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Configuration
public class SeedConfig {

    @Bean
    @Order(1)
    CommandLineRunner seed(
            EmployeeRepository employees,
            FxRateRepository fx) {

        return args -> {

            LocalDate date = LocalDate.of(2026, 1, 1);

            Map<String, BigDecimal> rates = Map.of(
                    "USD", BigDecimal.ONE,
                    "EUR", new BigDecimal("1.08"),
                    "GBP", new BigDecimal("1.27"),
                    "INR", new BigDecimal("0.0119"),
                    "SGD", new BigDecimal("0.78"),
                    "AUD", new BigDecimal("0.66")
            );

            rates.forEach((currency, rate) -> {
                if (fx.findByCurrencyAndRateDate(currency, date).isEmpty()) {
                    fx.save(new FxRate(currency, date, rate));
                }
            });

            // Employees already exist, so don't seed them again.
            if (employees.count() > 0) {
                return;
            }

            String[] countries = {
                    "India", "USA", "UK",
                    "Germany", "Singapore", "Australia"
            };

            String[] currencies = {
                    "INR", "USD", "GBP",
                    "EUR", "SGD", "AUD"
            };

            String[] departments = {
                    "Engineering", "Finance", "HR",
                    "Sales", "Operations", "Product"
            };

            for (int i = 1; i <= 10000; i++) {

                Employee e = new Employee();

                e.setEmployeeNumber(
                        String.format("ACME-%05d", i)
                );

                e.setFirstName("Employee");

                e.setLastName(
                        String.format("%05d", i)
                );

                e.setDepartment(
                        departments[(i - 1) % departments.length]
                );

                e.setCountry(
                        countries[(i - 1) % countries.length]
                );

                e.setCurrency(
                        currencies[(i - 1) % currencies.length]
                );

                e.setAnnualSalary(
                        new BigDecimal(
                                30000 + (i % 120) * 750
                        )
                );

                e.setEffectiveDate(date);

                employees.save(e);
            }
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner initializeAnalytics(
            AnalyticsService analyticsService,
            SalaryAnalyticsRepository analyticsRepository) {

        return args -> {

            if (analyticsRepository.count() == 0) {
                analyticsService.refresh();
            }
        };
    }
}