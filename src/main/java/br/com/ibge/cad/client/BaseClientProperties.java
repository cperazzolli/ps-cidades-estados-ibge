package br.com.ibge.cad.client;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
public class BaseClientProperties {

    private static final List<HttpStatus> DEFAULT_RETRIEVABLE_HTTP_STATUS = List.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatus.BAD_GATEWAY,
            HttpStatus.SERVICE_UNAVAILABLE,
            HttpStatus.GATEWAY_TIMEOUT
    );
    private static final Duration DEFAULT_MIN_BACKOFF = Duration.ofSeconds(2);
    private static final Duration DEFAULT_MAX_BACKOFF = Duration.ofSeconds(10);
    private static final int DEFAULT_MAX_ATTEMPTS = 3;
    private static final double DEFAULT_JITTER_FACTOR = 0.75;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);

    @NotBlank
    @URL
    private String baseUrl;
    private List<Integer> retrievableHttpStatusCode;
    private int retrievableMinBackoffInSeconds;
    private int retrievableMaxBackoffInSeconds;
    private int retrievableMaxAttempts;
    private double retrievableJitterFactor;
    private int timeoutInSeconds;

    public List<Integer> getRetrievableHttpStatusCode() {
        if (CollectionUtils.isEmpty(this.retrievableHttpStatusCode)) {
            return DEFAULT_RETRIEVABLE_HTTP_STATUS.stream().map(HttpStatus::value).toList();
        }

        return this.retrievableHttpStatusCode;
    }


    public List<HttpStatusCode> getRetrievableHttpErrors() {
        return getRetrievableHttpStatusCode().stream().map(HttpStatusCode::valueOf).toList();
    }

    public Duration getRetrievableMinBackoff() {
        return this.retrievableMinBackoffInSeconds <= 0
                ? DEFAULT_MIN_BACKOFF
                : Duration.ofSeconds(this.retrievableMinBackoffInSeconds);
    }

    public Duration getRetrievableMaxBackoff() {
        return this.retrievableMaxBackoffInSeconds <= 0
                ? DEFAULT_MAX_BACKOFF
                : Duration.ofSeconds(this.retrievableMaxBackoffInSeconds);
    }

    public int getRetrievableMaxAttempts() {
        return this.retrievableMaxAttempts <= 0
                ? DEFAULT_MAX_ATTEMPTS
                : this.retrievableMaxAttempts;
    }

    public double getRetrievableMaxJitterFactor() {
        return this.retrievableJitterFactor <= 0
                ? DEFAULT_JITTER_FACTOR
                : this.retrievableJitterFactor;
    }

    public Duration getTimeout() {
        return this.timeoutInSeconds <= 0
                ? DEFAULT_TIMEOUT
                : Duration.ofSeconds(this.timeoutInSeconds);
    }

    @PostConstruct
    public void validateProperties() {
        if (getRetrievableMinBackoff().compareTo(getRetrievableMaxBackoff()) > 0) {
            throw new IllegalStateException("Min backoff cannot be greater than max backoff");
        }
    }

    public static Duration getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }
}
