package br.com.ibge.cad.service;

import br.com.ibge.cad.client.estados.EstadosClient;
import br.com.ibge.cad.domain.EstadoResponse;
import br.com.ibge.cad.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class EstadosService {

    private final EstadosClient estadosClient;

    public EstadosService(final EstadosClient estadosClient) {
        this.estadosClient = estadosClient;
    }

    public EstadoResponse findEstados() {

        final var estadosResponse = estadosClient.find();

        if(estadosResponse == null) {
            throw new BusinessException("Estados não rotornados.");
        }

        return  estadosResponse.success();
    }
}
