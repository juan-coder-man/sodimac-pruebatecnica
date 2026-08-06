import { HistoryItem } from '../models';

const CSV_HEADERS = [
  'etqId',
  'lpnId',
  'zone',
  'requestedBy',
  'printedAt',
  'result',
  'eventType',
  'reason',
  'reprintReason'
] as const;

export function escapeCsvField(value: string | null | undefined): string {
  const text = value ?? '';
  if (/[",\n\r]/.test(text)) {
    return `"${text.replace(/"/g, '""')}"`;
  }
  return text;
}

export function historyToCsv(items: HistoryItem[]): string {
  const rows = items.map((item) =>
    [
      item.etqId,
      item.lpnId,
      item.zone,
      item.requestedBy,
      item.printedAt,
      item.result,
      item.eventType,
      item.reason,
      item.reprintReason
    ]
      .map(escapeCsvField)
      .join(',')
  );

  return [CSV_HEADERS.join(','), ...rows].join('\n');
}

export function downloadCsv(filename: string, content: string): void {
  const blob = new Blob(['\uFEFF' + content], { type: 'text/csv;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const anchor = document.createElement('a');
  anchor.href = url;
  anchor.download = filename;
  anchor.click();
  URL.revokeObjectURL(url);
}
