import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { ApiResponse, EtqDetail, PrintResponseData, UiFailure } from '../../core/models';
import { ApiCodes } from '../../core/utils/api-codes';
import { messageForCode, toUiFailure } from '../../core/utils/api-error.util';
import { EtqApiService } from '../../services/etq-api.service';
import { PrintApiService } from '../../services/print-api.service';

@Component({
  selector: 'app-print-page',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './print-page.html',
  styleUrl: './print-page.scss'
})
export class PrintPage {
  private readonly fb = inject(FormBuilder);
  private readonly printApi = inject(PrintApiService);
  private readonly etqApi = inject(EtqApiService);

  protected readonly apiUrl = environment.apiUrl;
  protected readonly submitting = signal(false);
  protected readonly consulting = signal(false);
  protected readonly etqDetail = signal<EtqDetail | null>(null);
  protected readonly consultaZplOpen = signal(false);
  protected readonly lastResponse = signal<ApiResponse<PrintResponseData> | null>(null);
  protected readonly lastFailure = signal<UiFailure | null>(null);
  protected readonly zplOpen = signal(false);
  protected readonly displayMessage = signal('');

  protected readonly form = this.fb.nonNullable.group({
    lpn: ['', Validators.required],
    zone: ['', Validators.required],
    requestedBy: ['', Validators.required],
    reprintReason: ['']
  });

  protected consultar(): void {
    const lpnControl = this.form.controls.lpn;
    const lpn = lpnControl.value.trim();

    if (!lpn) {
      lpnControl.markAsTouched();
      return;
    }

    this.consulting.set(true);
    this.etqDetail.set(null);
    this.consultaZplOpen.set(false);
    this.lastFailure.set(null);

    this.etqApi.consultar(lpn).subscribe({
      next: (res) => {
        this.consulting.set(false);

        if (res.success && res.data) {
          this.etqDetail.set(res.data);
          if (res.data.zone) {
            this.form.controls.zone.setValue(res.data.zone);
          }
          return;
        }

        this.etqDetail.set(null);
        this.lastFailure.set({
          code: res.code || ApiCodes.LPN_NOT_FOUND,
          message: messageForCode(res.code, res.message),
          fieldErrors: res.errors ?? []
        });
      },
      error: (err: unknown) => {
        this.consulting.set(false);
        this.etqDetail.set(null);
        this.lastFailure.set(toUiFailure(err));
      }
    });
  }

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();
    const reprintReason = raw.reprintReason.trim();

    this.submitting.set(true);
    this.lastResponse.set(null);
    this.lastFailure.set(null);
    this.displayMessage.set('');
    this.zplOpen.set(false);

    this.printApi
      .imprimir({
        lpn: raw.lpn.trim(),
        zone: raw.zone.trim(),
        requestedBy: raw.requestedBy.trim(),
        reprintReason: reprintReason.length > 0 ? reprintReason : null
      })
      .subscribe({
        next: (res) => {
          this.submitting.set(false);
          this.lastResponse.set(res);
          this.displayMessage.set(messageForCode(res.code, res.message));
        },
        error: (err: unknown) => {
          this.submitting.set(false);
          this.lastFailure.set(toUiFailure(err));
        }
      });
  }

  protected toggleZpl(): void {
    this.zplOpen.update((open) => !open);
  }

  protected toggleConsultaZpl(): void {
    this.consultaZplOpen.update((open) => !open);
  }

  protected isSuccessCode(code: string): boolean {
    return code === ApiCodes.PRINT_OK || code === ApiCodes.REPRINT_OK;
  }

  protected isReprint(code: string, eventType: string | null | undefined): boolean {
    return code === ApiCodes.REPRINT_OK || eventType === 'REIMPRESION';
  }

  protected isInvalidDocumentStatus(status: string): boolean {
    return status === 'ANULADA' || status === 'DEVUELTA';
  }

  protected showError(controlName: 'lpn' | 'zone' | 'requestedBy'): boolean {
    const control = this.form.controls[controlName];
    return control.invalid && control.touched;
  }
}
