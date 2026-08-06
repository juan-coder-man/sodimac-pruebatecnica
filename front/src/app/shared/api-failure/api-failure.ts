import { Component, input } from '@angular/core';
import { UiFailure } from '../../core/models';

@Component({
  selector: 'app-api-failure',
  standalone: true,
  templateUrl: './api-failure.html',
  styleUrl: './api-failure.scss'
})
export class ApiFailurePanel {
  readonly failure = input.required<UiFailure>();
  readonly heading = input('Error de solicitud');
}
