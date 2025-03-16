package br.com.ibge.cad.resource;

import br.com.ibge.cad.domain.EstadoResponse;
import br.com.ibge.cad.domain.MunicipioResponse;
import br.com.ibge.cad.service.EstadosService;
import br.com.ibge.cad.service.MunicipioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IbgeEstadosCidadesResource implements IbgeEstadosCidadesOpenApi{

    private final EstadosService estadosService;
    private final MunicipioService municipioService;

    public IbgeEstadosCidadesResource(EstadosService estadosService, MunicipioService municipioService) {
        this.estadosService = estadosService;
        this.municipioService = municipioService;
    }

    @Override
    public ResponseEntity<EstadoResponse> findAllEstados() {
        return ResponseEntity.ok(estadosService.findEstados());
    }

    @Override
    public ResponseEntity<MunicipioResponse> findMunicipios(final String uf) {
        return ResponseEntity.ok(municipioService.findMunicipio(uf));
    }
}
