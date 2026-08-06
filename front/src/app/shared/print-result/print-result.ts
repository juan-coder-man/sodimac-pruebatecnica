import { Component, input, output } from '@angular/core';
import { ApiResponse, PrintResponseData } from '../../core/models';
import { ApiCodes } from '../../core/utils/api-codes';

@Component({
  selector: 'app-print-result',
  standalone: true,
  templateUrl: './print-result.html',
  styleUrl: './print-result.scss'
})
export class PrintResultPanel {
  readonly response = input.required<ApiResponse<PrintResponseData>>();
  readonly displayMessage = input('');
  readonly zplOpen = input(false);
  readonly zplToggle = output<void>();

  protected isSuccessCode(code: string): boolean {
    return code === ApiCodes.PRINT_OK || code === ApiCodes.REPRINT_OK;
  }

  protected isReprint(code: string, eventType: string | null | undefined): boolean {
    return code === ApiCodes.REPRINT_OK || eventType === 'REIMPRESION';
  }

  protected onToggleZpl(): void {
    this.zplToggle.emit();
  }
}
