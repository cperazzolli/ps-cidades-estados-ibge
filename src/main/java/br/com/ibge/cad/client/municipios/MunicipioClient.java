package br.com.ibge.cad.client.municipios;

import br.com.ibge.cad.client.BaseClientProperties;
import br.com.ibge.cad.client.ClientError;
import br.com.ibge.cad.client.ClientResponse;
import br.com.ibge.cad.client.WebClientTemplate;
import br.com.ibge.cad.config.IbgeClientConfig.IbgeClientProperties;
import br.com.ibge.cad.domain.MunicipioResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.net.ssl.SSLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class MunicipioClient extends WebClientTemplate {

    protected MunicipioClient(@IbgeClientProperties final BaseClientProperties properties) throws SSLException {
        super(properties);
    }

    public ClientResponse<List<MunicipioResponse>, ClientError> execute(@NotBlank String ufId) {
        try {
            final var municipioResponse = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/localidades/estados/{UF}/municipios")
                            .build(ufId))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<MunicipioResponse>>() {
                    })
                    .timeout(timeoutInSeconds())
                    .retryWhen(retryBackoffSpec())
                    .onErrorResume(WebClientResponseException.class, fallbackResponseException())
                    .block();

            if (Objects.isNull(municipioResponse) || CollectionUtils.isEmpty(municipioResponse)) {
                return ClientResponse.withSuccess(Collections.emptyList());
            }

            return ClientResponse.withSuccess(municipioResponse);
        } catch (final WebClientResponseException ex) {
            return ClientResponse.withError(ClientError.with(ex.getMessage(), ex.getStatusCode()));
        }
    }
}
