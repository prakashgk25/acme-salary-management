
package com.acme.salary.service;

import com.acme.salary.entity.FxRate;
import com.acme.salary.repository.FxRateRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FxServiceTest {

    @Test
    void normalizesUsingLatestRate() {

        var repository = mock(FxRateRepository.class);

        when(repository
                .findTopByCurrencyAndRateDateLessThanEqualOrderByRateDateDesc(
                        "EUR",
                        LocalDate.of(2026, 1, 1)))
                .thenReturn(Optional.of(
                        new FxRate(
                                "EUR",
                                LocalDate.of(2026, 1, 1),
                                new BigDecimal("1.10")
                        )
                ));

        var service = new FxService(repository, "USD");

        assertThat(
                service.normalize(
                        "EUR",
                        new BigDecimal("1000"),
                        LocalDate.of(2026, 1, 1)
                )
        ).isEqualByComparingTo("1100.00");
    }

    @Test
    void usdIsUnchanged() {

        var repository = mock(FxRateRepository.class);

        var service = new FxService(repository, "USD");

        assertThat(
                service.normalize(
                        "USD",
                        new BigDecimal("1234.567"),
                        LocalDate.now()
                )
        ).isEqualByComparingTo("1234.57");

        verifyNoInteractions(repository);
    }
}
