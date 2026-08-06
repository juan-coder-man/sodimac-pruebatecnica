export interface EtqProduct {
  productCode: string;
  productDescription: string;
  requestedQty: number;
  uom: string;
}

export interface EtqDetail {
  idEtiqueta: string;
  purchaseOrder: string;
  tcOrderId: string;
  sku: string;
  unidades: number;
  zpl: string;
  lpnId: string;
  zone: string;
  documentStatus: string;
  products: EtqProduct[];
}
