package br.com.ibge.cad.client.municipios;

import br.com.ibge.cad.client.*;
import br.com.ibge.cad.config.IbgeClientConfig.IbgeClientProperties;
import br.com.ibge.cad.domain.MunicipioDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.net.ssl.SSLException;
import java.util.List;

@Component
@Slf4j
public class MunicipioClient extends WebClientTemplate {

    public MunicipioClient(@IbgeClientProperties final BaseClientProperties properties) throws SSLException {
        super(properties);
    }

    public ClientResult<List<MunicipioDTO>, ClientError> find(@NotBlank final String uf) {
        try {
            final List<MunicipioDTO> municipios = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/localidades/estados/{UF}/municipios").build(uf))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<MunicipioDTO>>(){})
                    .timeout(getTimeout())
                    .retryWhen(retryBackoffSpec())
                    .doOnError(error -> log.error("Error signal detected", error))
                    .onErrorResume(WebClientResponseException.class, fallbackResponseException())
                    .block();

            return new Success<>(municipios);
        } catch (final WebClientResponseException ex) {
            return ClientResult.fail(ClientError.with(ex.getMessage(), ex.getStatusCode()));
        }
    }
}
