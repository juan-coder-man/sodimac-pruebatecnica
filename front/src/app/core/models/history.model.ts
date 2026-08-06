export interface HistoryItem {
  etqId: string;
  lpnId: string;
  zone: string;
  requestedBy: string;
  printedAt: string;
  result: string;
  eventType: string;
  reason: string | null;
  reprintReason: string | null;
}

export interface HistoryFilters {
  lpn?: string;
  zone?: string;
  result?: 'EXITOSO' | 'RECHAZADO';
}
