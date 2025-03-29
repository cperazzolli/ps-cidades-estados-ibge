package br.com.ibge.cad;

import br.com.ibge.cad.util.JsonUtils;
import okhttp3.Headers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class WebClientIntegrationTest {

    protected static MockWebServer mockWebServer;
    protected static String baseUrl;
    protected static int minBackOffInSeconds;
    protected static int maxBackOffInSeconds;
    protected static int timeoutInSeconds;

    @BeforeEach
    protected void setUp() throws IOException {
       mockWebServer = new MockWebServer();
       mockWebServer.start();

       baseUrl = String.format("http://localhost:%s",
               mockWebServer.getPort());

       minBackOffInSeconds = 1;
       maxBackOffInSeconds = 1;
       timeoutInSeconds = 3;
    }

    @AfterEach
    protected void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    protected <T> void verifyRequestCount(
            int expectedCount,
            HttpMethod method,
            String path,
            Headers expectedHeaders,
            T expectedBody // pode ser null
    ) throws InterruptedException {
        int actualCount = 0;

        for (int i = 0; i < expectedCount; i++) {
            RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
            assertThat(request).as("Requisição %d não foi recebida", i + 1).isNotNull();

            assertThat(request.getMethod())
                    .as("Método HTTP inesperado")
                    .isEqualTo(method.name());

            assertThat(request.getPath())
                    .as("Path inesperado")
                    .isEqualTo(path);

            if (expectedHeaders != null) {
                assertThat(request.getHeaders())
                        .as("Headers diferentes do esperado")
                        .containsAll(expectedHeaders);
            }

            if (expectedBody != null) {
                String expectedJson = JsonUtils.writeValueAsString(expectedBody);
                assertThat(request.getBody().readUtf8())
                        .as("Body da requisição diferente")
                        .isEqualTo(expectedJson);
            }

            actualCount++;
        }

        // Garante que não houve requisições além do esperado
        assertThat(mockWebServer.takeRequest(100, TimeUnit.MILLISECONDS))
                .as("Mais requisições do que o esperado")
                .isNull();

        assertThat(actualCount)
                .as("Número de requisições diferente do esperado")
                .isEqualTo(expectedCount);
    }

    protected void enqueueJsonResponse(int statusCode, Object responseBody) {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(statusCode)
                        .addHeader("Content-Type", "application/json")
                        .setBody(JsonUtils.writeValueAsString(responseBody))
        );
    }
}
