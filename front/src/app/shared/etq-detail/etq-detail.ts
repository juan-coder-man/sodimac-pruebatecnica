import { Component, input, output, signal } from '@angular/core';
import { EtqDetail } from '../../core/models';

@Component({
  selector: 'app-etq-detail',
  standalone: true,
  templateUrl: './etq-detail.html',
  styleUrl: './etq-detail.scss'
})
export class EtqDetailPanel {
  readonly detail = input.required<EtqDetail>();
  readonly zplOpen = input(false);
  readonly zplToggle = output<void>();

  protected readonly zplCopied = signal(false);
  private copiedTimer: ReturnType<typeof setTimeout> | null = null;

  protected isInvalidDocumentStatus(status: string): boolean {
    return status === 'ANULADA' || status === 'DEVUELTA';
  }

  protected onToggleZpl(): void {
    this.zplToggle.emit();
  }

  protected async onCopyZpl(): Promise<void> {
    const zpl = this.detail().zpl;
    if (!zpl) {
      return;
    }
    try {
      await navigator.clipboard.writeText(zpl);
      this.zplCopied.set(true);
      if (this.copiedTimer) {
        clearTimeout(this.copiedTimer);
      }
      this.copiedTimer = setTimeout(() => this.zplCopied.set(false), 1500);
    } catch {
      // Sin permiso / contexto no seguro: no romper la UI
    }
  }
}
