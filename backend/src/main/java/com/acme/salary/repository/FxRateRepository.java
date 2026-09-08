package com.acme.salary.repository;

import com.acme.salary.entity.FxRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FxRateRepository extends JpaRepository<FxRate, Long> {

    Optional<FxRate> findByCurrencyAndRateDate(
            String currency,
            LocalDate rateDate
    );

    Optional<FxRate> findTopByCurrencyAndRateDateLessThanEqualOrderByRateDateDesc(
            String currency,
            LocalDate rateDate
    );
}