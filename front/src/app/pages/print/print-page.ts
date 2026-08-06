import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { ApiResponse, PrintResponseData, UiFailure } from '../../core/models';
import { ApiCodes } from '../../core/utils/api-codes';
import { messageForCode, toUiFailure } from '../../core/utils/api-error.util';
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

  protected readonly apiUrl = environment.apiUrl;
  protected readonly submitting = signal(false);
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

  protected isSuccessCode(code: string): boolean {
    return code === ApiCodes.PRINT_OK || code === ApiCodes.REPRINT_OK;
  }

  protected isReprint(code: string, eventType: string | null | undefined): boolean {
    return code === ApiCodes.REPRINT_OK || eventType === 'REIMPRESION';
  }

  protected showError(controlName: 'lpn' | 'zone' | 'requestedBy'): boolean {
    const control = this.form.controls[controlName];
    return control.invalid && control.touched;
  }
}
