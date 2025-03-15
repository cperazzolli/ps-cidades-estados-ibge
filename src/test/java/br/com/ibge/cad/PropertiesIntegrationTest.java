package br.com.ibge.cad;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("integration-test")
public @interface PropertiesIntegrationTest {

    @AliasFor(annotation = ContextConfiguration.class, attribute = "classes")
    Class<?>[] classes() default {};
}
