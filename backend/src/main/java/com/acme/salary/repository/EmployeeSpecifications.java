package com.acme.salary.repository;

import com.acme.salary.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecifications {

    private EmployeeSpecifications() {}

    public static Specification<Employee> search(String search, String country,
                                                   String department, String currency) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("employeeNumber")), pattern),
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern)
                ));
            }
            if (country != null)    predicates.add(cb.equal(root.get("country"), country));
            if (department != null) predicates.add(cb.equal(root.get("department"), department));
            if (currency != null)   predicates.add(cb.equal(root.get("currency"), currency));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}