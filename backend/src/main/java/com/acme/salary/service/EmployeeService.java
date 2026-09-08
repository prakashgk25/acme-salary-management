package com.acme.salary.service;

import com.acme.salary.dto.*;
import com.acme.salary.entity.Employee;
import com.acme.salary.exception.*;
import com.acme.salary.repository.EmployeeRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EmployeeService {

    private final EmployeeRepository repo;
    private final FxService fx;
    private final AnalyticsService analytics;

    public EmployeeService(
            EmployeeRepository r,
            FxService f,
            AnalyticsService analytics) {
        repo = r;
        fx = f;
        this.analytics = analytics;
    }

    private EmployeeResponse out(Employee e) {
        return new EmployeeResponse(
                e.getId(),
                e.getVersion(),
                e.getEmployeeNumber(),
                e.getFirstName(),
                e.getLastName(),
                e.getDepartment(),
                e.getCountry(),
                e.getCurrency(),
                e.getAnnualSalary(),
                fx.normalize(
                        e.getCurrency(),
                        e.getAnnualSalary(),
                        e.getEffectiveDate()
                ),
                e.getEffectiveDate()
        );
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> search(
            String s,
            String c,
            String d,
            String cur,
            Pageable p) {

        return repo.search(
                blank(s),
                blank(c),
                blank(d),
                blank(cur),
                p
        ).map(this::out);
    }

    private String blank(String s) {
        return s == null || s.isBlank() ? null : s;
    }

    @Transactional(readOnly = true)
    public EmployeeResponse get(Long id) {
        return out(
                repo.findById(id).orElseThrow(
                        () -> new NotFoundException("Employee not found")
                )
        );
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest r) {

        if (repo.findByEmployeeNumber(r.employeeNumber()).isPresent()) {
            throw new ConflictException("Employee number already exists");
        }

        Employee e = new Employee();
        apply(e, r);

        Employee saved = repo.save(e);

        analytics.refresh();

        return out(saved);
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest r) {

        Employee e = repo.findById(id).orElseThrow(
                () -> new NotFoundException("Employee not found")
        );

        if (!Objects.equals(e.getVersion(), r.version())) {
            throw new ConflictException("Stale employee version");
        }

        apply(e, r);

        Employee saved = repo.saveAndFlush(e);

        analytics.refresh();

        return out(saved);
    }

    @Transactional
    public void delete(Long id, Long version) {

        Employee e = repo.findById(id).orElseThrow(
                () -> new NotFoundException("Employee not found")
        );

        if (!Objects.equals(e.getVersion(), version)) {
            throw new ConflictException("Stale employee version");
        }

        repo.delete(e);
        repo.flush();

        analytics.refresh();
    }

    private void apply(Employee e, EmployeeRequest r) {

        e.setEmployeeNumber(r.employeeNumber());
        e.setFirstName(r.firstName());
        e.setLastName(r.lastName());
        e.setDepartment(r.department());
        e.setCountry(r.country());
        e.setCurrency(r.currency().toUpperCase());
        e.setAnnualSalary(r.annualSalary());
        e.setEffectiveDate(r.effectiveDate());
    }
}