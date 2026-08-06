import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, PrintRequest, PrintResponseData } from '../core/models';
import { handleApiCall } from '../core/utils/api-error.util';

@Injectable({
  providedIn: 'root'
})
export class PrintApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  imprimir(req: PrintRequest): Observable<ApiResponse<PrintResponseData>> {
    return handleApiCall(
      this.http.post<ApiResponse<PrintResponseData>>(`${this.baseUrl}/print`, req)
    );
  }
}
