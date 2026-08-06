import {
  Component,
  HostListener,
  output,
  signal
} from '@angular/core';
import {
  DEV_COMMON_FIELDS,
  DEV_TEST_SCENARIOS,
  DevTestField
} from './dev-test-scenarios';

@Component({
  selector: 'app-dev-test-data-panel',
  standalone: true,
  templateUrl: './dev-test-data-panel.html',
  styleUrl: './dev-test-data-panel.scss'
})
export class DevTestDataPanel {
  readonly closed = output<void>();

  protected readonly commonFields = DEV_COMMON_FIELDS;
  protected readonly scenarios = DEV_TEST_SCENARIOS;

  protected readonly left = signal(24);
  protected readonly top = signal(72);
  protected readonly copiedKey = signal<string | null>(null);

  private dragging = false;
  private dragOffsetX = 0;
  private dragOffsetY = 0;
  private copiedTimer: ReturnType<typeof setTimeout> | null = null;

  protected onClose(): void {
    this.closed.emit();
  }

  protected onDragStart(event: PointerEvent): void {
    const target = event.target as HTMLElement | null;
    if (target?.closest('button')) {
      return;
    }

    this.dragging = true;
    this.dragOffsetX = event.clientX - this.left();
    this.dragOffsetY = event.clientY - this.top();
    (event.currentTarget as HTMLElement).setPointerCapture(event.pointerId);
    event.preventDefault();
  }

  protected onDragMove(event: PointerEvent): void {
    if (!this.dragging) {
      return;
    }

    const panelMinVisible = 48;
    const maxLeft = Math.max(0, window.innerWidth - panelMinVisible);
    const maxTop = Math.max(0, window.innerHeight - panelMinVisible);

    this.left.set(
      Math.min(maxLeft, Math.max(0, event.clientX - this.dragOffsetX))
    );
    this.top.set(
      Math.min(maxTop, Math.max(0, event.clientY - this.dragOffsetY))
    );
  }

  protected onDragEnd(event: PointerEvent): void {
    if (!this.dragging) {
      return;
    }
    this.dragging = false;
    try {
      (event.currentTarget as HTMLElement).releasePointerCapture(event.pointerId);
    } catch {
      // capture ya liberado
    }
  }

  @HostListener('window:resize')
  protected onWindowResize(): void {
    const panelMinVisible = 48;
    const maxLeft = Math.max(0, window.innerWidth - panelMinVisible);
    const maxTop = Math.max(0, window.innerHeight - panelMinVisible);
    this.left.set(Math.min(this.left(), maxLeft));
    this.top.set(Math.min(this.top(), maxTop));
  }

  protected async onCopy(field: DevTestField, key: string): Promise<void> {
    try {
      await navigator.clipboard.writeText(field.value);
      this.copiedKey.set(key);
      if (this.copiedTimer) {
        clearTimeout(this.copiedTimer);
      }
      this.copiedTimer = setTimeout(() => this.copiedKey.set(null), 1500);
    } catch {
      // Sin permiso / contexto no seguro: no romper la UI
    }
  }

  protected copyLabel(key: string): string {
    return this.copiedKey() === key ? 'Copiado' : 'Copiar';
  }
}
