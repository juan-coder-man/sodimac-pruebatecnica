import { TestBed } from '@angular/core/testing';
import { FormGroup } from '@angular/forms';
import { of } from 'rxjs';
import { ApiCodes } from '../../core/utils/api-codes';
import { EtqApiService } from '../../services/etq-api.service';
import { PrintApiService } from '../../services/print-api.service';
import { PrintPage } from './print-page';

type PrintPageHarness = {
  form: FormGroup;
  submit: () => void;
};

describe('PrintPage', () => {
  let page: PrintPageHarness;
  let printApi: { imprimir: ReturnType<typeof vi.fn> };
  let etqApi: { consultar: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    printApi = { imprimir: vi.fn() };
    etqApi = { consultar: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [PrintPage],
      providers: [
        { provide: PrintApiService, useValue: printApi },
        { provide: EtqApiService, useValue: etqApi }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(PrintPage);
    page = fixture.componentInstance as unknown as PrintPageHarness;
    fixture.detectChanges();
  });

  it('no llama imprimir si el formulario es inválido', () => {
    page.submit();

    expect(printApi.imprimir).not.toHaveBeenCalled();
    expect(page.form.controls['lpn'].touched).toBe(true);
    expect(page.form.controls['zone'].touched).toBe(true);
    expect(page.form.controls['requestedBy'].touched).toBe(true);
  });

  it('llama imprimir con valores trim cuando el form es válido', () => {
    printApi.imprimir.mockReturnValue(
      of({
        success: true,
        code: ApiCodes.PRINT_OK,
        message: 'ok',
        data: null,
        errors: []
      })
    );

    page.form.setValue({
      lpn: '  LPN-000987654  ',
      zone: '  ZONA-PICKING-A  ',
      requestedBy: '  operador  ',
      reprintReason: ''
    });

    page.submit();

    expect(printApi.imprimir).toHaveBeenCalledTimes(1);
    expect(printApi.imprimir).toHaveBeenCalledWith({
      lpn: 'LPN-000987654',
      zone: 'ZONA-PICKING-A',
      requestedBy: 'operador',
      reprintReason: null
    });
  });
});
