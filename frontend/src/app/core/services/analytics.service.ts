import { Injectable, inject } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../environments/environment";
import { AnalyticsSummary } from "../../models/employee.model";

@Injectable({ providedIn: "root" })
export class AnalyticsService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/analytics`;

  summary(): Observable<AnalyticsSummary> {
    return this.http.get<AnalyticsSummary>(this.url);
  }
}
