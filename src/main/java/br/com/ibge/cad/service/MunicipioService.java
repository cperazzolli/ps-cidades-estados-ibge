package br.com.ibge.cad.service;

import br.com.ibge.cad.client.municipios.MunicipioClient;
import br.com.ibge.cad.domain.MunicipioResponse;
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

    public List<MunicipioResponse> findMunicipio(final @NotBlank String uf) {

        final var municipioResponse = municipioClient.execute(uf);

        if(municipioResponse == null) {
            throw new BusinessException("Estados não rotornados.");
        }

        return  municipioResponse.success();
    }
}
