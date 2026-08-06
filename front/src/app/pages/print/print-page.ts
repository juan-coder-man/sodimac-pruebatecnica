import { Component, inject, signal } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HealthApiService } from '../../services/health-api.service';

@Component({
  selector: 'app-print-page',
  standalone: true,
  templateUrl: './print-page.html',
  styleUrl: './print-page.scss'
})
export class PrintPage {
  private readonly healthApi = inject(HealthApiService);

  protected readonly apiUrl = environment.apiUrl;
  protected readonly checking = signal(false);
  protected readonly healthOk = signal<boolean | null>(null);
  protected readonly healthPayload = signal('');

  protected checkHealth(): void {
    this.checking.set(true);
    this.healthOk.set(null);
    this.healthPayload.set('');

    this.healthApi.check().subscribe({
      next: (res) => {
        this.checking.set(false);
        this.healthOk.set(true);
        this.healthPayload.set(JSON.stringify(res, null, 2));
      },
      error: (err: unknown) => {
        this.checking.set(false);
        this.healthOk.set(false);
        const message =
          err && typeof err === 'object' && 'message' in err
            ? String((err as { message: unknown }).message)
            : 'No se pudo conectar con el backend';
        this.healthPayload.set(message);
      }
    });
  }
}
