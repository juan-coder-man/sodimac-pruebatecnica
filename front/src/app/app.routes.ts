import { Routes } from '@angular/router';
import { PrintPage } from './pages/print/print-page';

export const routes: Routes = [
  { path: '', redirectTo: 'print', pathMatch: 'full' },
  { path: 'print', component: PrintPage },
  { path: '**', redirectTo: 'print' }
];
