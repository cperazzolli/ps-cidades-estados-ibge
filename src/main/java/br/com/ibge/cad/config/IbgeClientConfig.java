package br.com.ibge.cad.config;

import br.com.ibge.cad.client.BaseClientProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Configuration
public class IbgeClientConfig {

    @Bean
    @Validated
    @IbgeClientProperties
    @ConfigurationProperties("ibge.estados-client")
    public BaseClientProperties ibgeEstadosClientProperties() {
        return new BaseClientProperties();
    }

    @Target({
            ElementType.FIELD,
            ElementType.METHOD,
            ElementType.PARAMETER,
            ElementType.TYPE,
            ElementType.ANNOTATION_TYPE
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface IbgeClientProperties {}
}
