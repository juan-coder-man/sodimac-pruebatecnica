import { HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { ApiError, ApiResponse, UiFailure } from '../models';
import { API_CODE_MESSAGES, ApiCodes } from './api-codes';

export function messageForCode(code: string, fallback?: string): string {
  return API_CODE_MESSAGES[code] ?? fallback ?? code;
}

export function isApiResponse(value: unknown): value is ApiResponse<unknown> {
  return (
    typeof value === 'object' &&
    value !== null &&
    'success' in value &&
    typeof (value as ApiResponse<unknown>).success === 'boolean' &&
    'code' in value &&
    typeof (value as ApiResponse<unknown>).code === 'string'
  );
}

export function toUiFailure(err: unknown): UiFailure {
  if (err instanceof HttpErrorResponse) {
    const body = err.error;

    if (isApiResponse(body)) {
      return {
        code: body.code || ApiCodes.INTERNAL_ERROR,
        message: messageForCode(body.code, body.message || err.message),
        fieldErrors: normalizeFieldErrors(body.errors),
        httpStatus: err.status
      };
    }

    if (err.status === 0) {
      return {
        code: ApiCodes.INTERNAL_ERROR,
        message: 'No se pudo conectar con el backend',
        fieldErrors: [],
        httpStatus: 0
      };
    }

    return {
      code: ApiCodes.INTERNAL_ERROR,
      message: err.message || 'Error de comunicación con el backend',
      fieldErrors: [],
      httpStatus: err.status
    };
  }

  if (isUiFailure(err)) {
    return err;
  }

  return {
    code: ApiCodes.INTERNAL_ERROR,
    message: 'Error inesperado',
    fieldErrors: []
  };
}

export function handleApiCall<T>(source$: Observable<ApiResponse<T>>): Observable<ApiResponse<T>> {
  return source$.pipe(catchError((err: unknown) => throwError(() => toUiFailure(err))));
}

function normalizeFieldErrors(errors: ApiError[] | null | undefined): ApiError[] {
  return Array.isArray(errors) ? errors : [];
}

function isUiFailure(value: unknown): value is UiFailure {
  return (
    typeof value === 'object' &&
    value !== null &&
    'code' in value &&
    'message' in value &&
    'fieldErrors' in value
  );
}
