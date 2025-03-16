package br.com.ibge.cad.domain;

import br.com.ibge.cad.DataFaker;
import br.com.ibge.cad.JacksonIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JacksonIntegrationTest
class EstadoResponseTest {

    @Autowired
    private JacksonTester<EstadoResponse> jacksonTester;


    @Test
    void testMarshall() throws IOException {
        final var estadosResponse = DataFaker.getEstados();

        final var actual = this.jacksonTester.write(estadosResponse);

        assertThat(actual)
                .hasJsonPathNumberValue("$.data[0].id",estadosResponse.data().getFirst().id())
                .hasJsonPathStringValue("$.data[0].sigla",estadosResponse.data().getFirst().sigla())
                .hasJsonPathStringValue("$.data[0].nome",estadosResponse.data().getFirst().nome())
                .hasJsonPathNumberValue("$.data[0].regiao.id",estadosResponse.data().getFirst().regiao().id())
                .hasJsonPathStringValue("$.data[0].regiao.sigla",estadosResponse.data().getFirst().regiao().sigla())
                .hasJsonPathStringValue("$.data[0].regiao.nome",estadosResponse.data().getFirst().regiao().nome());
    }

    @Test
    void testUnMarshall() throws IOException {
        final var estadosResponse = DataFaker.getEstados();

        final var json = """ 
                {
                     "data" : [
                             {
                                 "id": %s,
                                 "sigla": "%s",
                                 "nome": "%s",
                                 "regiao": {
                                     "id": %s,
                                     "sigla": "%s",
                                     "nome": "%s"
                                 }
                             }
                         ]
                     }
                """.formatted(estadosResponse.data().getFirst().id(),
                              estadosResponse.data().getFirst().sigla(),
                              estadosResponse.data().getFirst().nome(),
                              estadosResponse.data().getFirst().regiao().id(),
                              estadosResponse.data().getFirst().regiao().sigla(),
                              estadosResponse.data().getFirst().regiao().nome());

        final var actual = jacksonTester.parse(json);

        assertThat(actual).isEqualTo(estadosResponse);
    }
}
