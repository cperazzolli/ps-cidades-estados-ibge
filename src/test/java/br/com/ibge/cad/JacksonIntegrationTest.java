package br.com.ibge.cad;

import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("integration-test")
@JsonTest(
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = TestObjectMapperConfig.class
                )
        }
)
public @interface JacksonIntegrationTest {
}
