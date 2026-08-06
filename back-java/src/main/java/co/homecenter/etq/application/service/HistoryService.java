package co.homecenter.etq.application.service;

import java.util.List;

import co.homecenter.etq.api.dto.response.HistoryItemResponse;
import co.homecenter.etq.domain.enums.PrintResult;

public interface HistoryService {

    List<HistoryItemResponse> listar(String lpn, String zone, PrintResult result);
}
