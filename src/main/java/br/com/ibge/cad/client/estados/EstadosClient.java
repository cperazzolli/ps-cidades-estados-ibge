package br.com.ibge.cad.client.estados;

import br.com.ibge.cad.client.*;
import br.com.ibge.cad.config.IbgeClientConfig.IbgeClientProperties;
import br.com.ibge.cad.domain.EstadoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.net.ssl.SSLException;
import java.util.List;

@Slf4j
@Component
public class EstadosClient extends WebClientTemplate {

    public EstadosClient(@IbgeClientProperties final BaseClientProperties properties) throws SSLException {
        super(properties);
    }

    public ClientResult<List<EstadoDTO>, ClientError> find() {
        try {
            final List<EstadoDTO> estados = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/localidades/estados").build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<EstadoDTO>>(){})
                    .timeout(getTimeout())
                    .retryWhen(retryBackoffSpec())
                    .doOnError(error -> log.error("Error signal detected", error))
                    .onErrorResume(WebClientResponseException.class, fallbackResponseException())
                    .block();

            return new Success<>(estados);
        } catch (final WebClientResponseException ex) {

            return ClientResult.fail(ClientError.with(ex.getMessage(), ex.getStatusCode()));
        }
    }
}
