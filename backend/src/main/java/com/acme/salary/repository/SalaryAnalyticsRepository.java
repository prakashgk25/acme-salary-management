package com.acme.salary.repository;

import com.acme.salary.entity.SalaryAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryAnalyticsRepository
                extends JpaRepository<SalaryAnalytics, Long> {
}