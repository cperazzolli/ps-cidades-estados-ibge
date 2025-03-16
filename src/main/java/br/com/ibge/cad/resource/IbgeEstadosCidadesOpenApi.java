package br.com.ibge.cad.resource;

import br.com.ibge.cad.domain.EstadoResponse;
import br.com.ibge.cad.domain.MunicipioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1")
public interface IbgeEstadosCidadesOpenApi {


    @Operation(summary = "Busca todos os estados do Brasil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os estados",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EstadoResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Chamada invalida",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Estados não encontrados",
                    content = @Content) })
    @GetMapping("/estados")
    ResponseEntity<EstadoResponse> findAllEstados();

    @Operation(summary = "Busca todos os municipios do Brasil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna os municiios",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MunicipioResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Chamada invalida",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Municipios não encontrados",
                    content = @Content) })
    @GetMapping("/municipios/{UF}")
    ResponseEntity<MunicipioResponse> findMunicipios(@PathVariable("UF") String uf);
}
