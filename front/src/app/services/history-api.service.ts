import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, HistoryFilters, HistoryItem } from '../core/models';
import { handleApiCall } from '../core/utils/api-error.util';

@Injectable({
  providedIn: 'root'
})
export class HistoryApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  listar(filters?: HistoryFilters): Observable<ApiResponse<HistoryItem[]>> {
    let params = new HttpParams();

    if (filters?.lpn) {
      params = params.set('lpn', filters.lpn);
    }
    if (filters?.zone) {
      params = params.set('zone', filters.zone);
    }
    if (filters?.result) {
      params = params.set('result', filters.result);
    }

    return handleApiCall(
      this.http.get<ApiResponse<HistoryItem[]>>(`${this.baseUrl}/print/history`, { params })
    );
  }
}
