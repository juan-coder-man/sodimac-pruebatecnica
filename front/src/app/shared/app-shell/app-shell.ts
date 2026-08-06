import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { environment } from '../../../environments/environment';
import { DevTestDataPanel } from '../dev-test-data-panel/dev-test-data-panel';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, DevTestDataPanel],
  templateUrl: './app-shell.html',
  styleUrl: './app-shell.scss'
})
export class AppShell {
  readonly isProduction = environment.production;
  protected readonly devPanelOpen = signal(false);

  protected openDevPanel(): void {
    this.devPanelOpen.set(true);
  }

  protected closeDevPanel(): void {
    this.devPanelOpen.set(false);
  }
}
