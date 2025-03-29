package br.com.ibge.cad.service;

import br.com.ibge.cad.client.ClientError;
import br.com.ibge.cad.client.ClientResult;
import br.com.ibge.cad.client.municipios.MunicipioClient;
import br.com.ibge.cad.domain.EstadoDTO;
import br.com.ibge.cad.domain.MunicipioDTO;
import br.com.ibge.cad.exception.BusinessException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioService {

    private final MunicipioClient municipioClient;


    public MunicipioService(MunicipioClient municipioClient) {
        this.municipioClient = municipioClient;
    }

    public List<MunicipioDTO> findMunicipio(final @NotBlank String uf) {

        final ClientResult<List<MunicipioDTO>, ClientError> result = municipioClient.find(uf);

        if (result.isSuccess()) {
            return result.success();
        }

        final var erro = result.error();
        throw new BusinessException("Erro ao buscar estados: " + erro.message() + " (status: " + erro.statusCode() + ")");
    }
}
