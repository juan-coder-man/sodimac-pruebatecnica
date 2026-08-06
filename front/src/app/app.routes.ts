import { Routes } from '@angular/router';
import { HistoryPage } from './pages/history/history-page';
import { PrintPage } from './pages/print/print-page';

export const routes: Routes = [
  { path: '', redirectTo: 'print', pathMatch: 'full' },
  { path: 'print', component: PrintPage },
  { path: 'history', component: HistoryPage },
  { path: '**', redirectTo: 'print' }
];
