import { HttpErrorResponse } from '@angular/common/http';
import { ApiCodes } from './api-codes';
import { isApiResponse, messageForCode, toUiFailure } from './api-error.util';

describe('api-error.util', () => {
  describe('messageForCode', () => {
    it('devuelve el mensaje en español para PRINT_OK', () => {
      expect(messageForCode(ApiCodes.PRINT_OK)).toBe('Impresión exitosa');
    });

    it('usa el fallback cuando el código no existe', () => {
      expect(messageForCode('UNKNOWN', 'fallback')).toBe('fallback');
    });

    it('usa el código cuando no hay fallback', () => {
      expect(messageForCode('UNKNOWN')).toBe('UNKNOWN');
    });
  });

  describe('isApiResponse', () => {
    it('reconoce un envelope válido', () => {
      expect(
        isApiResponse({
          success: true,
          code: 'ETQ_FOUND',
          message: 'ok',
          data: null,
          errors: []
        })
      ).toBe(true);
    });

    it('rechaza valores inválidos', () => {
      expect(isApiResponse(null)).toBe(false);
      expect(isApiResponse({})).toBe(false);
      expect(isApiResponse({ success: 'yes', code: 1 })).toBe(false);
    });
  });

  describe('toUiFailure', () => {
    it('mapea HttpErrorResponse con envelope', () => {
      const failure = toUiFailure(
        new HttpErrorResponse({
          status: 404,
          error: {
            success: false,
            code: ApiCodes.LPN_NOT_FOUND,
            message: 'No encontrado',
            data: null,
            errors: [{ field: 'lpn', code: 'NOT_FOUND', message: 'LPN inválido' }]
          }
        })
      );

      expect(failure.code).toBe(ApiCodes.LPN_NOT_FOUND);
      expect(failure.message).toBe('LPN no encontrado en los datos mock');
      expect(failure.httpStatus).toBe(404);
      expect(failure.fieldErrors).toHaveLength(1);
      expect(failure.fieldErrors[0].field).toBe('lpn');
    });

    it('mapea status 0 a error de conexión', () => {
      const failure = toUiFailure(
        new HttpErrorResponse({
          status: 0,
          statusText: 'Unknown Error',
          error: new ProgressEvent('error')
        })
      );

      expect(failure.code).toBe(ApiCodes.INTERNAL_ERROR);
      expect(failure.message).toBe('No se pudo conectar con el backend');
      expect(failure.httpStatus).toBe(0);
    });

    it('mapea error genérico a INTERNAL_ERROR', () => {
      const failure = toUiFailure('boom');
      expect(failure.code).toBe(ApiCodes.INTERNAL_ERROR);
      expect(failure.message).toBe('Error inesperado');
      expect(failure.fieldErrors).toEqual([]);
    });
  });
});
