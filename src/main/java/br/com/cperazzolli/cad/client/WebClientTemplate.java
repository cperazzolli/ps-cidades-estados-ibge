package br.com.cperazzolli.cad.client;

import br.com.cperazzolli.cad.config.ObjectMapperConfig;
import br.com.cperazzolli.cad.exception.RetryExhaustedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
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
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

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
    private final int timeoutInSeconds;

    ObjectMapper objectMapper = new ObjectMapper();

    protected WebClientTemplate(final BaseClientProperties properties) throws SSLException {
        this.baseUrl = properties.getBaseUrl();
        this.retrievableHttpStatusCodes = properties.getRetrievableHttpErrors();
        this.retrievableMinBackoffInSeconds = properties.getRetrievableMinBackoffInSeconds();
        this.retrievableMaxBackoffInSeconds = properties.getRetrievableMaxBackoffInSeconds();
        this.retrievableMaxAttemps = properties.getRetrievableMaxAttempts();
        this.retrievableJitterFactor = properties.getRetrievableMaxJitterFactor();
        this.timeoutInSeconds = properties.getTimeoutInSeconds();

        final SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        final HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Duration.ofSeconds(this.timeoutInSeconds).toMillisPart())
                .secure(contextSpec -> contextSpec.sslContext(sslContext))
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(this.timeoutInSeconds));
                    connection.addHandlerLast(new WriteTimeoutHandler(this.timeoutInSeconds));
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
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> throwsetryExchaustedException(retrySignal.totalRetries(), retrySignal.failure()));

    }

    protected Duration timeoutInSeconds() {
        return Duration.ofSeconds(this.timeoutInSeconds);
    }

    protected <T> Function<WebClientResponseException, Mono<T>> fallbackResponseException() {
        return responseException -> {
            if (HttpStatus.NOT_FOUND == HttpStatus.valueOf(responseException.getStatusCode().value())) {
                return Mono.empty();
            }

            if (HttpStatus.BAD_REQUEST == HttpStatus.valueOf(responseException.getStatusCode().value())) {
                final var responseBodyError = responseException.getResponseBodyAsString(StandardCharsets.UTF_8);
                if (StringUtils.isNotBlank(responseBodyError)) {
                    log.warn("Trecho não recuperado");
                    /*DefaultRecuperaProxy recuperaErrorResponse = objectMapper.readValue(responseBodyError, DefaultRecuperaProxy.class);
                    final var recuperaErrorResponse = Optional.ofNullable(responseBodyError, DefaultRecuperaProxy);/*
                    return recuperaErrorResponse.isPresent() && recuperaErrorResponse.get().isRecuperaInternalError() ? Mono.empty() : recuperaErrorResponse.get().isRecuperaInternalError() ;*/
                    return Mono.empty();
                }

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

        log.warn("Retrying client '{}':{}/{}", this.baseUrl, totalRetries + 1, this.retrievableMaxAttemps, failure);
    }

    protected Throwable throwsetryExchaustedException(final long totalRetries, final Throwable throwable) {
        final String message = String.format("Retry client '%s' attemps", this.baseUrl, totalRetries);
        return new RetryExhaustedException(message, throwable);
    }
}
