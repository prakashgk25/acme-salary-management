package com.acme.salary.service;

import com.acme.salary.exception.NotFoundException;
import com.acme.salary.repository.FxRateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class FxService {

        private final FxRateRepository repo;
        private final String base;

        public FxService(
                        FxRateRepository repo,
                        @Value("${app.base-currency:USD}") String base) {
                this.repo = repo;
                this.base = base;
        }

        public BigDecimal normalize(
                        String currency,
                        BigDecimal salary,
                        LocalDate date) {

                if (base.equalsIgnoreCase(currency)) {
                        return salary.setScale(2, RoundingMode.HALF_UP);
                }

                var rate = repo
                                .findTopByCurrencyAndRateDateLessThanEqualOrderByRateDateDesc(
                                                currency,
                                                date)
                                .orElseThrow(() -> new NotFoundException(
                                                "FX rate not found for " + currency));

                return salary
                                .multiply(rate.getToUsd())
                                .setScale(2, RoundingMode.HALF_UP);
        }

}
