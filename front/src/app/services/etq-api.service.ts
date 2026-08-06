import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ApiResponse, EtqDetail } from '../core/models';
import { handleApiCall } from '../core/utils/api-error.util';

@Injectable({
  providedIn: 'root'
})
export class EtqApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  consultar(lpn: string): Observable<ApiResponse<EtqDetail>> {
    return handleApiCall(
      this.http.post<ApiResponse<EtqDetail>>(`${this.baseUrl}/etq/consulta`, {
        request: { lpn }
      })
    );
  }
}
