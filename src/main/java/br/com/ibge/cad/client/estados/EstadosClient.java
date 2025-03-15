package br.com.ibge.cad.client.estados;

import br.com.ibge.cad.client.BaseClientProperties;
import br.com.ibge.cad.client.ClientError;
import br.com.ibge.cad.client.ClientResponse;
import br.com.ibge.cad.client.WebClientTemplate;
import br.com.ibge.cad.config.IbgeClientConfig.IbgeClientProperties;
import br.com.ibge.cad.domain.EstadoResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.net.ssl.SSLException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class EstadosClient extends WebClientTemplate {
    protected EstadosClient(@IbgeClientProperties final BaseClientProperties properties) throws SSLException {
        super(properties);
    }

    public ClientResponse<List<EstadoResponse>,ClientError> find(){

        try {
            final List<EstadoResponse> estadosResponse = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/localidades/estados")
                    .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<EstadoResponse>>(){})
                    .timeout(timeoutInSeconds())
                    .retryWhen(retryBackoffSpec())
                    .onErrorResume(WebClientResponseException.class, fallbackResponseException())
                    .block();

            if(Objects.isNull(estadosResponse) || CollectionUtils.isEmpty(estadosResponse)){
                return ClientResponse.withSuccess(Collections.emptyList());
            }

            return ClientResponse.withSuccess(estadosResponse);
        } catch(final WebClientResponseException ex) {
            return ClientResponse.withError(ClientError.with(ex.getMessage(),ex.getStatusCode()));
        }

    }
}
