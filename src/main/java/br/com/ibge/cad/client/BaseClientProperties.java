package br.com.ibge.cad.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
public class BaseClientProperties {

    private static final List<Integer> DEFAULT_RETRIVABLE_HTTP_STATUS_CODES = List.of(500,502,503,504);
    private static final int DEFAULT_MIN_BACKOFF_IN_SECONDS = 2;
    private static final int DEFAULT_MAX_BACKOFF_IN_SECONDS = 10;
    private static final int DEFAULT_MAX_ATTEMPTS = 3;
    private static final double DEFAULT_JITTER_FACTOR = 0.75;
    private static final int DEFAULT_TIMEOUT_IN_SECONDS = 60;

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
        if(CollectionUtils.isEmpty(this.retrievableHttpStatusCode)){
            return DEFAULT_RETRIVABLE_HTTP_STATUS_CODES;
        }

        return this.retrievableHttpStatusCode;
    }

    public List<HttpStatusCode> getRetrievableHttpErrors() {
        if(CollectionUtils.isEmpty(this.retrievableHttpStatusCode)){
            return DEFAULT_RETRIVABLE_HTTP_STATUS_CODES.stream().map(HttpStatusCode::valueOf).toList();
        }

        return this.retrievableHttpStatusCode.stream().map(HttpStatusCode::valueOf).toList();
    }

    public int getRetrievableMinBackoffInSeconds() {
        return this.retrievableMinBackoffInSeconds == 0 ? DEFAULT_MIN_BACKOFF_IN_SECONDS : this.retrievableMinBackoffInSeconds;
    }

    public int getRetrievableMaxBackoffInSeconds() {
        return this.retrievableMaxBackoffInSeconds == 0 ? DEFAULT_MAX_BACKOFF_IN_SECONDS : this.retrievableMaxBackoffInSeconds;
    }

    public int getRetrievableMaxAttempts() {
        return this.retrievableMaxAttempts == 0 ? DEFAULT_MAX_ATTEMPTS : this.retrievableMaxAttempts;
    }

    public double getRetrievableMaxJitterFactor() {
        return this.retrievableJitterFactor == 0 ? DEFAULT_JITTER_FACTOR : this.retrievableJitterFactor;
    }

    public int getTimeoutInSeconds() {
        return this.timeoutInSeconds == 0 ? DEFAULT_TIMEOUT_IN_SECONDS : this.timeoutInSeconds;
    }
}
