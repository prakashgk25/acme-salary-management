package com.acme.salary.controller; 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.data.web.PageableDefault;
import com.acme.salary.dto.*; import com.acme.salary.service.EmployeeService; import jakarta.validation.Valid; import org.springframework.data.domain.*; import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = {
        "http://localhost:4200",
        "https://acme-salary-management-ui.onrender.com"
})
@RestController
@RequestMapping("/api/employees")
public class EmployeeController { private final EmployeeService s; public EmployeeController(EmployeeService s){this.s=s;}
 @GetMapping public Page<EmployeeResponse> search(@RequestParam(required=false) String search,@RequestParam(required=false) String country,@RequestParam(required=false) String department,@RequestParam(required=false) String currency,@PageableDefault(size=25) Pageable pageable){return s.search(search,country,department,currency,pageable);}
 @GetMapping("/{id}") public EmployeeResponse get(@PathVariable Long id){return s.get(id);} @PostMapping public EmployeeResponse create(@Valid @RequestBody EmployeeRequest r){return s.create(r);} @PutMapping("/{id}") public EmployeeResponse update(@PathVariable Long id,@Valid @RequestBody EmployeeRequest r){return s.update(id,r);} @DeleteMapping("/{id}") public void delete(@PathVariable Long id,@RequestHeader("If-Match-Version") Long version){s.delete(id,version);}
}
