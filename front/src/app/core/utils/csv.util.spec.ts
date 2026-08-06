import { HistoryItem } from '../models';
import { escapeCsvField, historyToCsv } from './csv.util';

describe('csv.util', () => {
  describe('escapeCsvField', () => {
    it('deja valores simples sin comillas', () => {
      expect(escapeCsvField('LPN-001')).toBe('LPN-001');
    });

    it('escapa comas y comillas dobles', () => {
      expect(escapeCsvField('a,b')).toBe('"a,b"');
      expect(escapeCsvField('dice "hola"')).toBe('"dice ""hola"""');
    });

    it('trata null/undefined como vacío', () => {
      expect(escapeCsvField(null)).toBe('');
      expect(escapeCsvField(undefined)).toBe('');
    });
  });

  describe('historyToCsv', () => {
    it('incluye header y una fila', () => {
      const items: HistoryItem[] = [
        {
          etqId: 'ETQ-1',
          lpnId: 'LPN-1',
          zone: 'ZONA-A',
          requestedBy: 'operador',
          printedAt: '2026-01-01T00:00:00Z',
          result: 'EXITOSO',
          eventType: 'IMPRESION',
          reason: null,
          reprintReason: null
        }
      ];

      const csv = historyToCsv(items);
      const lines = csv.split('\n');

      expect(lines[0]).toBe(
        'etqId,lpnId,zone,requestedBy,printedAt,result,eventType,reason,reprintReason'
      );
      expect(lines[1]).toContain('ETQ-1,LPN-1,ZONA-A,operador');
      expect(lines[1]).toContain('EXITOSO,IMPRESION,,');
    });

    it('escapa reason con comas', () => {
      const items: HistoryItem[] = [
        {
          etqId: 'ETQ-2',
          lpnId: 'LPN-2',
          zone: 'Z',
          requestedBy: 'u',
          printedAt: 't',
          result: 'RECHAZADO',
          eventType: 'IMPRESION',
          reason: 'motivo, con coma',
          reprintReason: null
        }
      ];

      expect(historyToCsv(items)).toContain('"motivo, con coma"');
    });
  });
});
