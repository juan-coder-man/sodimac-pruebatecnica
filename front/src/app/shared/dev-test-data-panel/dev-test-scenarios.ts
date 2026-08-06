export interface DevTestField {
  label: string;
  value: string;
}

export interface DevTestScenario {
  id: string;
  title: string;
  description: string;
  fields: DevTestField[];
}

export const DEV_COMMON_FIELDS: DevTestField[] = [
  { label: 'Usuario', value: 'usuario.operacion' },
  { label: 'Motivo reimpresión', value: 'Etiqueta danada' }
];

export const DEV_TEST_SCENARIOS: DevTestScenario[] = [
  {
    id: 'print-ok',
    title: 'Consulta / Print OK',
    description: 'ETQ_FOUND → PRINT_OK',
    fields: [
      { label: 'LPN', value: 'LPN-000987654' },
      { label: 'Zona', value: 'ZONA-PICKING-A' },
      { label: 'Usuario', value: 'usuario.operacion' }
    ]
  },
  {
    id: 'reprint',
    title: 'Reimpresión',
    description: 'Tras Print OK en la misma sesión → REPRINT_OK',
    fields: [
      { label: 'LPN', value: 'LPN-000987654' },
      { label: 'Zona', value: 'ZONA-PICKING-A' },
      { label: 'Usuario', value: 'usuario.operacion' },
      { label: 'Motivo', value: 'Etiqueta danada' }
    ]
  },
  {
    id: 'anulado',
    title: 'Documento anulado',
    description: 'DOCUMENT_INVALID_STATUS',
    fields: [
      { label: 'LPN', value: 'LPN-ANULADA-001' },
      { label: 'Zona', value: 'ZONA-PICKING-A' },
      { label: 'Usuario', value: 'usuario.operacion' }
    ]
  },
  {
    id: 'devuelta',
    title: 'Documento devuelto',
    description: 'DOCUMENT_INVALID_STATUS',
    fields: [
      { label: 'LPN', value: 'LPN-DEVUELTA-001' },
      { label: 'Zona', value: 'ZONA-PICKING-B' },
      { label: 'Usuario', value: 'usuario.operacion' }
    ]
  },
  {
    id: 'sin-stock',
    title: 'Sin abastecer / sin stock',
    description: 'PRODUCT_NOT_SUPPLIED (semilla)',
    fields: [
      { label: 'LPN', value: 'LPN-SIN-STOCK-001' },
      { label: 'Zona', value: 'ZONA-PICKING-C' },
      { label: 'Usuario', value: 'usuario.operacion' }
    ]
  },
  {
    id: 'lpn-inexistente',
    title: 'LPN inexistente',
    description: 'Consulta 404 / print LPN_NOT_FOUND',
    fields: [
      { label: 'LPN', value: 'LPN-NO-EXISTE' },
      { label: 'Zona', value: 'ZONA-PICKING-A' },
      { label: 'Usuario', value: 'usuario.operacion' }
    ]
  },
  {
    id: 'historial',
    title: 'Historial',
    description: 'Filtros tras imprimir al menos una vez',
    fields: [
      { label: 'LPN', value: 'LPN-000987654' },
      { label: 'Resultado', value: 'EXITOSO' },
      { label: 'Resultado (alt)', value: 'RECHAZADO' }
    ]
  }
];
