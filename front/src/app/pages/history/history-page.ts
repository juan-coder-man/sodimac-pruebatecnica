import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { HistoryFilters, HistoryItem, UiFailure } from '../../core/models';
import { toUiFailure } from '../../core/utils/api-error.util';
import { HistoryApiService } from '../../services/history-api.service';
import { ApiFailurePanel } from '../../shared/api-failure/api-failure';

@Component({
  selector: 'app-history-page',
  standalone: true,
  imports: [ReactiveFormsModule, ApiFailurePanel],
  templateUrl: './history-page.html',
  styleUrl: './history-page.scss'
})
export class HistoryPage implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly historyApi = inject(HistoryApiService);

  protected readonly loading = signal(false);
  protected readonly items = signal<HistoryItem[]>([]);
  protected readonly failure = signal<UiFailure | null>(null);

  protected readonly filtersForm = this.fb.nonNullable.group({
    lpn: [''],
    zone: [''],
    result: ['' as '' | 'EXITOSO' | 'RECHAZADO']
  });

  ngOnInit(): void {
    this.load();
  }

  protected load(): void {
    this.loading.set(true);
    this.failure.set(null);

    this.historyApi.listar(this.buildFilters()).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.success) {
          this.items.set(res.data ?? []);
          return;
        }
        this.items.set([]);
        this.failure.set({
          code: res.code,
          message: res.message,
          fieldErrors: res.errors ?? []
        });
      },
      error: (err: unknown) => {
        this.loading.set(false);
        this.items.set([]);
        this.failure.set(toUiFailure(err));
      }
    });
  }

  protected clearFilters(): void {
    this.filtersForm.reset({
      lpn: '',
      zone: '',
      result: ''
    });
    this.load();
  }

  protected isSuccessResult(result: string): boolean {
    return result === 'EXITOSO';
  }

  protected isReprint(eventType: string): boolean {
    return eventType === 'REIMPRESION';
  }

  private buildFilters(): HistoryFilters | undefined {
    const raw = this.filtersForm.getRawValue();
    const filters: HistoryFilters = {};

    const lpn = raw.lpn.trim();
    const zone = raw.zone.trim();

    if (lpn) {
      filters.lpn = lpn;
    }
    if (zone) {
      filters.zone = zone;
    }
    if (raw.result === 'EXITOSO' || raw.result === 'RECHAZADO') {
      filters.result = raw.result;
    }

    return Object.keys(filters).length > 0 ? filters : undefined;
  }
}
