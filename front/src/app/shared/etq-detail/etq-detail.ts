import { Component, input, output } from '@angular/core';
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

  protected isInvalidDocumentStatus(status: string): boolean {
    return status === 'ANULADA' || status === 'DEVUELTA';
  }

  protected onToggleZpl(): void {
    this.zplToggle.emit();
  }
}
