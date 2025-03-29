package br.com.ibge.cad.service;

import br.com.ibge.cad.client.ClientError;
import br.com.ibge.cad.client.ClientResult;
import br.com.ibge.cad.client.estados.EstadosClient;
import br.com.ibge.cad.domain.EstadoDTO;
import br.com.ibge.cad.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadosService {

    private final EstadosClient estadosClient;

    public EstadosService(final EstadosClient estadosClient) {
        this.estadosClient = estadosClient;
    }


    public List<EstadoDTO> findEstados() {

        final ClientResult<List<EstadoDTO>, ClientError> result = estadosClient.find();

        if (result.isSuccess()) {
            return result.success();
        }

        // Aqui você pode personalizar a exceção conforme o erro retornado
        final var erro = result.error();
        throw new BusinessException("Erro ao buscar estados: " + erro.message() + " (status: " + erro.statusCode() + ")");
    }
}
