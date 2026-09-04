package com.acme.salary.repository;
import com.acme.salary.entity.FxRate;
import org.springframework.data.jpa.repository.*;
import java.time.LocalDate;
import java.util.Optional;
public interface FxRateRepository extends JpaRepository<FxRate,Long> {
    Optional<FxRate> findTopByCurrencyAndRateDateLessThanEqualOrderByRateDateDesc(String currency, LocalDate date);
}
