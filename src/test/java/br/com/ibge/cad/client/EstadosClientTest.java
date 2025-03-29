package br.com.ibge.cad.client;

import br.com.ibge.cad.DataFaker;
import br.com.ibge.cad.WebClientIntegrationTest;
import br.com.ibge.cad.client.estados.EstadosClient;
import br.com.ibge.cad.domain.EstadoDTO;
import br.com.ibge.cad.exception.RetryExhaustedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.net.ssl.SSLException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstadosClientTest extends WebClientIntegrationTest {

    private EstadosClient subject;

    @BeforeEach
    void setup() throws SSLException {
        final var baseClientProperties = new BaseClientProperties();
        baseClientProperties.setBaseUrl(baseUrl);
        baseClientProperties.setRetrievableMinBackoffInSeconds(minBackOffInSeconds);
        baseClientProperties.setRetrievableMaxBackoffInSeconds(maxBackOffInSeconds);
        baseClientProperties.setTimeoutInSeconds(timeoutInSeconds);

        this.subject = new EstadosClient(baseClientProperties);
    }


    @Test
    void shouldReturnEstadosSuccessfully_whenIBGEResponds200() throws JsonProcessingException, InterruptedException {
        List<EstadoDTO> estados = DataFaker.getEstados(3);
        ObjectMapper objectMapper = new ObjectMapper();
        final var estadoResponse = objectMapper.writeValueAsString(estados);
        final var request = new MockResponse()
                .setBody(estadoResponse)
                .setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        mockWebServer.enqueue(request);
        final var actual = subject.find();
        enqueueJsonResponse(200, estados);
        assertThat(actual.success()).isEqualTo(estados);
        verifyRequestCount(1, HttpMethod.GET,"/api/v1/localidades/estados",null,null);
    }

    @Test
    void givenValidCall_whenCallState_thenReturnBadRequest() throws JsonProcessingException, InterruptedException {
        final String urlEsperada = String.format("%s/api/v1/localidades/estados", baseUrl);
        final var response = ClientResult.fail(new ClientError("400 Bad Request from GET " + urlEsperada,HttpStatus.BAD_REQUEST));
        final var request = new MockResponse()
                .setResponseCode(HttpStatus.BAD_REQUEST.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        mockWebServer.enqueue(request);
        final var actualErro = subject.find();
        assertThat(actualErro.error()).isEqualTo(response.error());
        verifyRequestCount(1, HttpMethod.GET,"/api/v1/localidades/estados",null,null);
    }

    @Test
    void givenValidCall_whenCallState_thenInternalServerError() throws JsonProcessingException, InterruptedException {
        final String urlEsperada = String.format("%s/api/v1/localidades/estados", baseUrl);
        final var response = ClientResponse.withError(
                new ClientError("500 Internal Server Error from GET " + urlEsperada, HttpStatus.INTERNAL_SERVER_ERROR));

        final var request = new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        mockWebServer.enqueue(request);
        mockWebServer.enqueue(request);
        mockWebServer.enqueue(request);
        mockWebServer.enqueue(request);


        RetryExhaustedException exception = assertThrows(RetryExhaustedException.class, () -> {
            subject.find();
        });

        assertThat(exception.getMessage())
                .contains("Retry client")
                .contains("exhausted after 3 attempts");

        verifyRequestCount(4, HttpMethod.GET, "/api/v1/localidades/estados",null,null);
    }

}
