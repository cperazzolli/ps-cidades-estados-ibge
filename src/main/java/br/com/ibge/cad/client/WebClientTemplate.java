package br.com.ibge.cad.client;

import br.com.ibge.cad.config.ObjectMapperConfig;
import br.com.ibge.cad.exception.RetryExhaustedException;
import br.com.ibge.cad.util.DefaultRecuperaProxy;
import br.com.ibge.cad.util.JsonUtils;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static br.com.ibge.cad.client.BaseClientProperties.getDefaultTimeout;


@Slf4j
public abstract class WebClientTemplate {

    private static final int DEFAULT_BUFFER_BYTES_SIZE = 5 * 1024 * 1024; //5MB

    protected final WebClient webClient;

    private final String baseUrl;
    private final List<HttpStatusCode> retrievableHttpStatusCodes;
    private final int retrievableMinBackoffInSeconds;
    private final int retrievableMaxBackoffInSeconds;
    private final int retrievableMaxAttemps;
    private final double retrievableJitterFactor;
    private final Integer timeout;

    protected WebClientTemplate(final BaseClientProperties properties) throws SSLException {
        this.baseUrl = properties.getBaseUrl();
        this.retrievableHttpStatusCodes = properties.getRetrievableHttpErrors();
        this.retrievableMinBackoffInSeconds = properties.getRetrievableMinBackoffInSeconds();
        this.retrievableMaxBackoffInSeconds = properties.getRetrievableMaxBackoffInSeconds();
        this.retrievableMaxAttemps = properties.getRetrievableMaxAttempts();
        this.retrievableJitterFactor = properties.getRetrievableMaxJitterFactor();
        this.timeout = properties.getTimeoutInSeconds();

        final SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        final HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) getTimeout().toMillis())
                .secure(contextSpec -> contextSpec.sslContext(sslContext))
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler((int) getTimeout().toSeconds()));
                    connection.addHandlerLast(new WriteTimeoutHandler((int) getTimeout().toSeconds()));
                });

        final var encoder = new Jackson2JsonEncoder(ObjectMapperConfig.objectMapper());
        final var decoder = new Jackson2JsonDecoder(ObjectMapperConfig.objectMapper());

        final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(codecConfig -> {
                    codecConfig.defaultCodecs().jackson2JsonEncoder(encoder);
                    codecConfig.defaultCodecs().jackson2JsonDecoder(decoder);
                    codecConfig.defaultCodecs().maxInMemorySize(DEFAULT_BUFFER_BYTES_SIZE);
                }).build();

        this.webClient = WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeaders(headers -> headers.addAll(defaultHeaders()))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    protected RetryBackoffSpec retryBackoffSpec() {
        return Retry.backoff(this.retrievableMaxAttemps, Duration.ofSeconds(this.retrievableMinBackoffInSeconds))
                .maxBackoff(Duration.ofSeconds(this.retrievableMaxBackoffInSeconds))
                .jitter(this.retrievableJitterFactor)
                .filter(this::allowRetry)
                .doBeforeRetry(retrySignal -> logRetry(retrySignal.totalRetries(), retrySignal.failure()))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> throwRetryExhaustedException(retrySignal.totalRetries(), retrySignal.failure()));

    }

    protected <T> Function<WebClientResponseException, Mono<T>> fallbackResponseException() {
        return responseException -> {
            final var status = responseException.getStatusCode();
            if (status == HttpStatus.NOT_FOUND) {
                return Mono.empty();
            }
            if (status == HttpStatus.BAD_REQUEST) {
                Mono.error(responseException);
                /*final var responseBodyError = responseException.getMessage();
                return Optional.ofNullable(JsonUtils.readValue(responseBodyError, DefaultRecuperaProxy.class))
                        .filter(DefaultRecuperaProxy::isRecuperaInternalError)
                        .map(error -> Mono.<T>empty())
                        .orElseGet(() -> Mono.error(responseException));*/
            }

            // Tratamento adicional para INTERNAL_SERVER_ERROR
            if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.warn("Retrying client {} : - cause: {}", this.baseUrl, responseException.getCause());
                return Mono.error(new IllegalStateException("Erro interno do servidor ao acessar o recurso externo."));
            }

            return Mono.error(responseException);
        };
    }

    protected boolean allowRetry(final Throwable ex) {
        if (ex instanceof WebClientResponseException exception) {
            return this.retrievableHttpStatusCodes.contains(exception.getStatusCode());
        }
        return false;
    }

    protected void logRetry(final long totalRetries, final Throwable failure) {
        log.warn("Retrying client '{}': attempt {}/{} - cause: {}", this.baseUrl, totalRetries + 1, this.retrievableMaxAttemps, failure.getMessage());
    }

    protected Throwable throwRetryExhaustedException(final long totalRetries, final Throwable throwable) {
        final String message = String.format("Retry client '%s' exhausted after %d attempts", this.baseUrl, totalRetries);
        return new RetryExhaustedException(message, throwable);
    }

    public Duration getTimeout() {
        if (timeout == null || timeout <= 0) {
            return getDefaultTimeout();
        }
        return Duration.ofSeconds(timeout);
    }

    protected HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
