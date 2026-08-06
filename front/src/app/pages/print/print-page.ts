import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { ApiResponse, EtqDetail, PrintResponseData, UiFailure } from '../../core/models';
import { ApiCodes } from '../../core/utils/api-codes';
import { messageForCode, toUiFailure } from '../../core/utils/api-error.util';
import { EtqApiService } from '../../services/etq-api.service';
import { PrintApiService } from '../../services/print-api.service';
import { ApiFailurePanel } from '../../shared/api-failure/api-failure';
import { EtqDetailPanel } from '../../shared/etq-detail/etq-detail';
import { PrintResultPanel } from '../../shared/print-result/print-result';

const SERVER_FIELD_NAMES = ['lpn', 'zone', 'requestedBy', 'reprintReason'] as const;

@Component({
  selector: 'app-print-page',
  standalone: true,
  imports: [ReactiveFormsModule, EtqDetailPanel, PrintResultPanel, ApiFailurePanel],
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
    this.clearServerErrors();

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
        const failure: UiFailure = {
          code: res.code || ApiCodes.LPN_NOT_FOUND,
          message: messageForCode(res.code, res.message),
          fieldErrors: res.errors ?? []
        };
        this.lastFailure.set(failure);
        this.applyServerFieldErrors(failure);
      },
      error: (err: unknown) => {
        this.consulting.set(false);
        this.etqDetail.set(null);
        const failure = toUiFailure(err);
        this.lastFailure.set(failure);
        this.applyServerFieldErrors(failure);
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
    this.clearServerErrors();

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
          const failure = toUiFailure(err);
          this.lastFailure.set(failure);
          this.applyServerFieldErrors(failure);
        }
      });
  }

  protected toggleZpl(): void {
    this.zplOpen.update((open) => !open);
  }

  protected toggleConsultaZpl(): void {
    this.consultaZplOpen.update((open) => !open);
  }

  protected showError(controlName: 'lpn' | 'zone' | 'requestedBy'): boolean {
    const control = this.form.controls[controlName];
    return !!control.errors?.['required'] && control.touched;
  }

  protected serverError(controlName: (typeof SERVER_FIELD_NAMES)[number]): string | null {
    const control = this.form.controls[controlName];
    const server = control.errors?.['server'];
    return typeof server === 'string' ? server : null;
  }

  private clearServerErrors(): void {
    for (const name of SERVER_FIELD_NAMES) {
      const control = this.form.controls[name];
      this.removeServerError(control);
    }
  }

  private applyServerFieldErrors(failure: UiFailure): void {
    for (const err of failure.fieldErrors) {
      if (!err.field) {
        continue;
      }
      const name = err.field as (typeof SERVER_FIELD_NAMES)[number];
      if (!SERVER_FIELD_NAMES.includes(name)) {
        continue;
      }
      const control = this.form.controls[name];
      control.setErrors({ ...control.errors, server: err.message });
      control.markAsTouched();
    }
  }

  private removeServerError(control: AbstractControl): void {
    if (!control.errors?.['server']) {
      return;
    }
    const { server: _server, ...rest } = control.errors;
    control.setErrors(Object.keys(rest).length > 0 ? rest : null);
  }
}
