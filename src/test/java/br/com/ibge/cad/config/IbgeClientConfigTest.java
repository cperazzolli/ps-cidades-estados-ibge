package br.com.ibge.cad.config;

import br.com.ibge.cad.IntegrationTest;
import br.com.ibge.cad.client.BaseClientProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class IbgeClientConfigTest {


    @Autowired
    @IbgeClientConfig.IbgeClientProperties
    private BaseClientProperties properties;

    @Test
    void givenContext_whenLoad_thenBeanShouldBeInjectedWithProperties() {
        assertThat(properties)
                .isNotNull();

        assertThat(properties.getBaseUrl())
                .isEqualTo("http://localhost:8080");
    }

}
