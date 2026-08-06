import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { ApiCodes } from '../core/utils/api-codes';
import { ApiResponse, PrintResponseData } from '../core/models';
import { PrintApiService } from './print-api.service';

describe('PrintApiService', () => {
  let service: PrintApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), PrintApiService]
    });
    service = TestBed.inject(PrintApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('POST /print con PrintRequest', () => {
    const request = {
      lpn: 'LPN-000987654',
      zone: 'ZONA-PICKING-A',
      requestedBy: 'operador',
      reprintReason: null
    };

    let body: ApiResponse<PrintResponseData> | undefined;
    service.imprimir(request).subscribe((res) => {
      body = res;
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/print`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);

    req.flush({
      success: true,
      code: ApiCodes.PRINT_OK,
      message: 'ok',
      data: { lpnId: request.lpn, result: 'EXITOSO' },
      errors: []
    });

    expect(body?.success).toBe(true);
    expect(body?.code).toBe(ApiCodes.PRINT_OK);
  });

  it('emite en next cuando HTTP 200 y success false', () => {
    let nextValue: ApiResponse<PrintResponseData> | undefined;
    let errored = false;

    service
      .imprimir({
        lpn: 'LPN-ANULADA-001',
        zone: 'ZONA-PICKING-A',
        requestedBy: 'operador'
      })
      .subscribe({
        next: (res) => {
          nextValue = res;
        },
        error: () => {
          errored = true;
        }
      });

    const req = httpMock.expectOne(`${environment.apiUrl}/print`);
    req.flush({
      success: false,
      code: ApiCodes.DOCUMENT_INVALID_STATUS,
      message: 'Documento anulado',
      data: { result: 'RECHAZADO' },
      errors: []
    });

    expect(errored).toBe(false);
    expect(nextValue?.success).toBe(false);
    expect(nextValue?.code).toBe(ApiCodes.DOCUMENT_INVALID_STATUS);
  });
});
