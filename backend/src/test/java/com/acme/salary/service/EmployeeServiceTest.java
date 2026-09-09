package com.acme.salary.service;

import com.acme.salary.entity.Employee;
import com.acme.salary.repository.EmployeeRepository;
import com.acme.salary.dto.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class EmployeeServiceTest {

        @Test
        void rejectsStaleVersion() {

                var repo = mock(EmployeeRepository.class);
                var fx = mock(FxService.class);
                var analytics = mock(AnalyticsService.class);

                var e = new Employee();
                e.setEmployeeNumber("A");

                when(repo.findById(1L)).thenReturn(Optional.of(e));

                var s = new EmployeeService(repo, fx, analytics);

                var req = new EmployeeRequest(
                                "A",
                                "F",
                                "L",
                                "D",
                                "India",
                                "INR",
                                new BigDecimal("100"),
                                LocalDate.now(),
                                99L);

                assertThatThrownBy(() -> s.update(1L, req))
                                .isInstanceOf(
                                                com.acme.salary.exception.ConflictException.class);

                verify(repo, never()).saveAndFlush(any());
        }
}