export interface PrintRequest {
  lpn: string;
  zone: string;
  requestedBy: string;
  reprintReason?: string | null;
}

export interface PrintResponseData {
  requestId: string;
  etqId: string;
  lpnId: string;
  zone: string;
  eventType: string;
  result: string;
  reason: string | null;
  zpl: string | null;
  printedAt: string;
  reprintReason: string | null;
}
