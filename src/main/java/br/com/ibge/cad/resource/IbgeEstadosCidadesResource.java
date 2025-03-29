package br.com.ibge.cad.resource;

import br.com.ibge.cad.domain.EstadoDTO;
import br.com.ibge.cad.domain.MunicipioDTO;
import br.com.ibge.cad.service.EstadosService;
import br.com.ibge.cad.service.MunicipioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class IbgeEstadosCidadesResource implements IbgeEstadosCidadesOpenApi{

    private final EstadosService estadosService;
    private final MunicipioService municipioService;

    public IbgeEstadosCidadesResource(EstadosService estadosService, MunicipioService municipioService) {
        this.estadosService = estadosService;
        this.municipioService = municipioService;
    }

    @Override
    public ResponseEntity<List<EstadoDTO>> findAllEstados() {
        return ResponseEntity.ok(estadosService.findEstados());
    }

    @Override
    public ResponseEntity<List<MunicipioDTO>> findMunicipios(final String uf) {
        return ResponseEntity.ok(municipioService.findMunicipio(uf));
    }
}
