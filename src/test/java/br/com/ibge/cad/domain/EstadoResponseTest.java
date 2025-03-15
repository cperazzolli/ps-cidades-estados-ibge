package br.com.ibge.cad.domain;

import br.com.ibge.cad.DataFaker;
import br.com.ibge.cad.JacksonIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JacksonIntegrationTest
class EstadoResponseTest {

    @Autowired
    private JacksonTester<List<EstadoResponse>> jacksonTester;


    @Test
    void testMarshall() throws IOException {
        final var estadosResponse = List.of(DataFaker.getEstados());

        final var actual = this.jacksonTester.write(estadosResponse);

        assertThat(actual)
                .hasJsonPathNumberValue("$[0].id",estadosResponse.getFirst().id());
    }
}
