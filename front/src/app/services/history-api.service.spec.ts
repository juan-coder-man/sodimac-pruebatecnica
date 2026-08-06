import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { ApiCodes } from '../core/utils/api-codes';
import { HistoryApiService } from './history-api.service';

describe('HistoryApiService', () => {
  let service: HistoryApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), HistoryApiService]
    });
    service = TestBed.inject(HistoryApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('GET /print/history con query lpn y result', () => {
    let count = 0;
    service.listar({ lpn: 'LPN-000987654', result: 'EXITOSO' }).subscribe((res) => {
      count = res.data?.length ?? 0;
    });

    const req = httpMock.expectOne(
      (r) =>
        r.url === `${environment.apiUrl}/print/history` &&
        r.params.get('lpn') === 'LPN-000987654' &&
        r.params.get('result') === 'EXITOSO'
    );
    expect(req.request.method).toBe('GET');

    req.flush({
      success: true,
      code: ApiCodes.HISTORY_OK,
      message: 'ok',
      data: [{ lpnId: 'LPN-000987654', result: 'EXITOSO' }],
      errors: []
    });

    expect(count).toBe(1);
  });
});
