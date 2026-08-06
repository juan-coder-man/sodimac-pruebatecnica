import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { ApiCodes } from '../core/utils/api-codes';
import { UiFailure } from '../core/models';
import { EtqApiService } from './etq-api.service';

describe('EtqApiService', () => {
  let service: EtqApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), EtqApiService]
    });
    service = TestBed.inject(EtqApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('POST /etq/consulta con body request.lpn', () => {
    let body: unknown;
    service.consultar('LPN-000987654').subscribe((res) => {
      body = res;
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/etq/consulta`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ request: { lpn: 'LPN-000987654' } });

    req.flush({
      success: true,
      code: ApiCodes.ETQ_FOUND,
      message: 'ok',
      data: { idEtiqueta: 'ETQ-1', lpnId: 'LPN-000987654' },
      errors: []
    });

    expect(body).toMatchObject({ success: true, code: ApiCodes.ETQ_FOUND });
  });

  it('propaga UiFailure en 404', () => {
    let failure: UiFailure | undefined;
    service.consultar('LPN-NO-EXISTE').subscribe({
      error: (err: UiFailure) => {
        failure = err;
      }
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/etq/consulta`);
    req.flush(
      {
        success: false,
        code: ApiCodes.LPN_NOT_FOUND,
        message: 'No encontrado',
        data: null,
        errors: []
      },
      { status: 404, statusText: 'Not Found' }
    );

    expect(failure?.code).toBe(ApiCodes.LPN_NOT_FOUND);
    expect(failure?.httpStatus).toBe(404);
  });
});
