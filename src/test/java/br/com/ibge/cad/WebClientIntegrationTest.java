package br.com.ibge.cad;

import br.com.ibge.cad.util.JsonUtils;
import okhttp3.Headers;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    protected static void setUp() throws IOException {
       mockWebServer = new MockWebServer();
       mockWebServer.start();

       baseUrl = String.format("http://localhost:%s",
               mockWebServer.getPort());

       minBackOffInSeconds = 1;
       maxBackOffInSeconds = 1;
       timeoutInSeconds = 3;
    }

    @AfterAll
    protected static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    protected static void varifyRequestsAmout(
            final int expectedAmountOfRequests,
            final Headers expectedHeaders,
            final HttpMethod expectedHttpmethod,
            final String expectedPath
            ) throws InterruptedException {

        int interactions =0;

        for(int i = 0; i <= expectedAmountOfRequests; i++) {
            final RecordedRequest recordedRequest = mockWebServer.takeRequest();
            assertThat(recordedRequest.getHeaders()).containsAll(expectedHeaders);
            assertThat(recordedRequest.getMethod()).isEqualTo(expectedHttpmethod.name());
            assertThat(recordedRequest.getPath()).isEqualTo(expectedPath);
            interactions++;
        }

        assertThat(mockWebServer.takeRequest(100, TimeUnit.MILLISECONDS)).isNull();
        assertThat(interactions).isEqualTo(expectedAmountOfRequests);
    }

    protected static <T> void varifyRequestsAmout(
            final int expectedAmountOfRequests,
            final Headers expectedHeaders,
            final HttpMethod expectedHttpmethod,
            final T body,
            final String expectedPath
    ) throws InterruptedException {
        int interactions =0;

        for(int i = 0; i <= expectedAmountOfRequests; i++) {
            final RecordedRequest recordedRequest = mockWebServer.takeRequest();
            assertThat(recordedRequest.getHeaders()).containsAll(expectedHeaders);
            assertThat(recordedRequest.getMethod()).isEqualTo(expectedHttpmethod.name());
            assertThat(recordedRequest.getBody().readUtf8()).isEqualTo(JsonUtils.writeValueAsString(body));
            assertThat(recordedRequest.getPath()).isEqualTo(expectedPath);
            interactions++;
        }

        assertThat(interactions).isEqualTo(expectedAmountOfRequests);
    }

    protected static <T> void varifyRequestsAmout(
            final int expectedAmountOfRequests,
            final Headers expectedHeaders,
            final HttpMethod expectedHttpmethod,
            final T body
    ) throws InterruptedException {
        int interactions =0;

        for(int i = 0; i <= expectedAmountOfRequests; i++) {
            final RecordedRequest recordedRequest = mockWebServer.takeRequest();
            assertThat(recordedRequest.getHeaders()).containsAll(expectedHeaders);
            assertThat(recordedRequest.getMethod()).isEqualTo(expectedHttpmethod.name());
            assertThat(recordedRequest.getBody().readUtf8()).isEqualTo(JsonUtils.writeValueAsString(body));
            interactions++;
        }

        assertThat(interactions).isEqualTo(expectedAmountOfRequests);
    }

    protected static <T> void varifyRequestsAmout(
            final int expectedAmountOfRequests,
            final HttpMethod expectedHttpmethod,
            final String expectedPath
    ) throws InterruptedException {
        int interactions =0;

        for(int i = 0; i <= expectedAmountOfRequests; i++) {
            final RecordedRequest recordedRequest = mockWebServer.takeRequest();
            assertThat(recordedRequest.getMethod()).isEqualTo(expectedHttpmethod.name());
            assertThat(recordedRequest.getPath()).isEqualTo(expectedPath);
            interactions++;
        }

        assertThat(interactions).isEqualTo(expectedAmountOfRequests);
    }
}
