package com.acme.salary.service;

import com.acme.salary.dto.AnalyticsResponse;
import com.acme.salary.entity.Employee;
import com.acme.salary.entity.FxRate;
import com.acme.salary.entity.SalaryAnalytics;
import com.acme.salary.repository.EmployeeRepository;
import com.acme.salary.repository.FxRateRepository;
import com.acme.salary.repository.SalaryAnalyticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private static final long ANALYTICS_ID = 1L;

    private final EmployeeRepository employeeRepository;
    private final SalaryAnalyticsRepository analyticsRepository;
    private final FxRateRepository fxRateRepository;

    public AnalyticsService(
            EmployeeRepository employeeRepository,
            SalaryAnalyticsRepository analyticsRepository,
            FxRateRepository fxRateRepository) {

        this.employeeRepository = employeeRepository;
        this.analyticsRepository = analyticsRepository;
        this.fxRateRepository = fxRateRepository;
    }

    @Transactional
    public void refresh() {

        List<Employee> employees =
                employeeRepository.findAllForAnalytics();

        Map<String, BigDecimal> rates = new HashMap<>();

        for (FxRate fxRate : fxRateRepository.findAll()) {
            rates.put(fxRate.getCurrency(), fxRate.getToUsd());
        }

        long totalEmployees = employees.size();

        BigDecimal totalPayroll = BigDecimal.ZERO;
        BigDecimal minimumSalary = null;
        BigDecimal maximumSalary = null;

        for (Employee employee : employees) {

            BigDecimal rate = rates.get(employee.getCurrency());

            if (rate == null) {
                throw new IllegalStateException(
                        "FX rate not found for currency: "
                                + employee.getCurrency()
                );
            }

            BigDecimal normalizedSalary =
                    employee.getAnnualSalary()
                            .multiply(rate)
                            .setScale(2, RoundingMode.HALF_UP);

            totalPayroll = totalPayroll.add(normalizedSalary);

            if (minimumSalary == null ||
                    normalizedSalary.compareTo(minimumSalary) < 0) {
                minimumSalary = normalizedSalary;
            }

            if (maximumSalary == null ||
                    normalizedSalary.compareTo(maximumSalary) > 0) {
                maximumSalary = normalizedSalary;
            }
        }

        if (minimumSalary == null) {
            minimumSalary = BigDecimal.ZERO;
        }

        if (maximumSalary == null) {
            maximumSalary = BigDecimal.ZERO;
        }

        BigDecimal averageSalary =
                totalEmployees == 0
                        ? BigDecimal.ZERO
                        : totalPayroll.divide(
                                BigDecimal.valueOf(totalEmployees),
                                2,
                                RoundingMode.HALF_UP
                        );

        SalaryAnalytics analytics =
                analyticsRepository.findById(ANALYTICS_ID)
                        .orElseGet(() -> {
                            SalaryAnalytics a =
                                    new SalaryAnalytics();
                            a.setId(ANALYTICS_ID);
                            return a;
                        });

        analytics.setTotalEmployees(totalEmployees);
        analytics.setTotalPayrollUsd(
                totalPayroll.setScale(2, RoundingMode.HALF_UP)
        );
        analytics.setAverageSalaryUsd(averageSalary);
        analytics.setMinimumSalaryUsd(
                minimumSalary.setScale(2, RoundingMode.HALF_UP)
        );
        analytics.setMaximumSalaryUsd(
                maximumSalary.setScale(2, RoundingMode.HALF_UP)
        );
        analytics.setCalculatedAt(LocalDateTime.now());

        analyticsRepository.saveAndFlush(analytics);
    }

    @Transactional
    public AnalyticsResponse summary() {

        SalaryAnalytics analytics =
                analyticsRepository.findById(ANALYTICS_ID)
                        .orElse(null);

        if (analytics == null) {
            refresh();

            analytics =
                    analyticsRepository.findById(ANALYTICS_ID)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Analytics summary not available"
                                    )
                            );
        }

        return new AnalyticsResponse(
                analytics.getTotalEmployees(),
                analytics.getAverageSalaryUsd(),
                analytics.getTotalPayrollUsd()
        );
    }
}