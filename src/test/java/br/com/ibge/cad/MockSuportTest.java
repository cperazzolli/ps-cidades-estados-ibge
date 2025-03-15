package br.com.ibge.cad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public abstract class MockSuportTest {

    protected abstract List<Object> mocks();

    @BeforeEach
    void cleanUp() {
        if(CollectionUtils.isEmpty(mocks())) {
            Mockito.reset(mocks().toArray());
        }
        MDC.clear();
    }
}
