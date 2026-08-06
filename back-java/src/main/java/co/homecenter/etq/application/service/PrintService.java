package co.homecenter.etq.application.service;

import co.homecenter.etq.api.dto.request.PrintRequest;
import co.homecenter.etq.api.dto.response.ApiResponse;
import co.homecenter.etq.api.dto.response.PrintResponseData;

public interface PrintService {

    ApiResponse<PrintResponseData> imprimir(PrintRequest request);
}
