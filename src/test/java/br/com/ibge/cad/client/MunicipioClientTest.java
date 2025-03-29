package br.com.ibge.cad.client;

import br.com.ibge.cad.WebClientIntegrationTest;
import br.com.ibge.cad.client.estados.EstadosClient;
import br.com.ibge.cad.client.municipios.MunicipioClient;
import org.junit.jupiter.api.BeforeEach;

import javax.net.ssl.SSLException;

public class MunicipioClientTest extends WebClientIntegrationTest {

    private MunicipioClient subject;

    @BeforeEach
    void setup() throws SSLException {
        final var baseClientProperties = new BaseClientProperties();
        baseClientProperties.setBaseUrl(baseUrl);
        baseClientProperties.setRetrievableMinBackoffInSeconds(minBackOffInSeconds);
        baseClientProperties.setRetrievableMaxBackoffInSeconds(maxBackOffInSeconds);
        baseClientProperties.setTimeoutInSeconds(timeoutInSeconds);

        this.subject = new MunicipioClient(baseClientProperties);
    }
}
