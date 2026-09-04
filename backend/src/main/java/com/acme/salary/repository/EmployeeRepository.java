package com.acme.salary.repository;

import com.acme.salary.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e
            FROM Employee e
            WHERE (
                :search IS NULL
                OR LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            AND (:country IS NULL OR e.country = :country)
            AND (:department IS NULL OR e.department = :department)
            AND (:currency IS NULL OR e.currency = :currency)
            """)
    Page<Employee> search(
            @Param("search") String search,
            @Param("country") String country,
            @Param("department") String department,
            @Param("currency") String currency,
            Pageable pageable
    );

    Optional<Employee> findByEmployeeNumber(String employeeNumber);
}

