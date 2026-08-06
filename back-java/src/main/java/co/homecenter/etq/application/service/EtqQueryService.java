package co.homecenter.etq.application.service;

import java.util.Optional;

import co.homecenter.etq.api.dto.response.EtqDetailResponse;

public interface EtqQueryService {

    Optional<EtqDetailResponse> consultarPorLpn(String lpn);
}
