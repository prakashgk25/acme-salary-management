package com.acme.salary.service;
import com.acme.salary.dto.AnalyticsResponse; import com.acme.salary.repository.EmployeeRepository; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.math.*;
@Service public class AnalyticsService { private final EmployeeRepository repo; private final FxService fx; public AnalyticsService(EmployeeRepository r,FxService f){repo=r;fx=f;}
 @Transactional(readOnly=true) public AnalyticsResponse summary(){var all=repo.findAll(); BigDecimal total=BigDecimal.ZERO; for(var e:all) total=total.add(fx.normalize(e.getCurrency(),e.getAnnualSalary(),e.getEffectiveDate())); var avg=all.isEmpty()?BigDecimal.ZERO:total.divide(BigDecimal.valueOf(all.size()),2,RoundingMode.HALF_UP); return new AnalyticsResponse(all.size(),avg,total.setScale(2,RoundingMode.HALF_UP));}
}
