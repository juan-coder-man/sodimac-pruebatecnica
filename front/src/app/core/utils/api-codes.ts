export const ApiCodes = {
  ETQ_FOUND: 'ETQ_FOUND',
  PRINT_OK: 'PRINT_OK',
  REPRINT_OK: 'REPRINT_OK',
  HISTORY_OK: 'HISTORY_OK',
  LPN_NOT_FOUND: 'LPN_NOT_FOUND',
  DOCUMENT_INVALID_STATUS: 'DOCUMENT_INVALID_STATUS',
  INSUFFICIENT_INVENTORY: 'INSUFFICIENT_INVENTORY',
  PRODUCT_NOT_SUPPLIED: 'PRODUCT_NOT_SUPPLIED',
  VALIDATION_ERROR: 'VALIDATION_ERROR',
  INTERNAL_ERROR: 'INTERNAL_ERROR'
} as const;

export type ApiCode = (typeof ApiCodes)[keyof typeof ApiCodes];

export const API_CODE_MESSAGES: Record<string, string> = {
  [ApiCodes.ETQ_FOUND]: 'ETQ encontrada',
  [ApiCodes.PRINT_OK]: 'Impresión exitosa',
  [ApiCodes.REPRINT_OK]: 'Reimpresión exitosa',
  [ApiCodes.HISTORY_OK]: 'Historial obtenido',
  [ApiCodes.LPN_NOT_FOUND]: 'LPN no encontrado en los datos mock',
  [ApiCodes.DOCUMENT_INVALID_STATUS]: 'El documento origen está anulado o devuelto',
  [ApiCodes.INSUFFICIENT_INVENTORY]: 'Stock insuficiente en la zona solicitada',
  [ApiCodes.PRODUCT_NOT_SUPPLIED]: 'Producto no abastecido en la zona',
  [ApiCodes.VALIDATION_ERROR]: 'La solicitud contiene datos inválidos',
  [ApiCodes.INTERNAL_ERROR]: 'Error interno del servidor'
};
