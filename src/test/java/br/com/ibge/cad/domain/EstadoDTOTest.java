package br.com.ibge.cad.domain;

import br.com.ibge.cad.DataFaker;
import br.com.ibge.cad.JacksonIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JacksonIntegrationTest
class EstadoDTOTest {

    @Autowired
    private JacksonTester<EstadoDTO> jacksonTester;


    @Test
    void shouldSerializeEstadoDTO() throws IOException {
        final var estado = DataFaker.getEstados(1);

        final var actual = this.jacksonTester.write(estado.get(0));

        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(estado.get(0).id());

        assertThat(actual).extractingJsonPathStringValue("$.sigla").isEqualTo(estado.get(0).sigla());

        assertThat(actual).extractingJsonPathStringValue("$.nome").isEqualTo(estado.get(0).nome());

        assertThat(actual).extractingJsonPathNumberValue("$.regiao.id").isEqualTo(estado.get(0).regiao().id());

        assertThat(actual).extractingJsonPathStringValue("$.regiao.sigla").isEqualTo(estado.get(0).regiao().sigla());

        assertThat(actual).extractingJsonPathStringValue("$.regiao.nome").isEqualTo(estado.get(0).regiao().nome());
    }

    @Test
    void shouldDeserializeEstadoDTO() throws IOException {
        final var estadosResponse = DataFaker.getEstados(1);

        final var json = """ 
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
                """.formatted(estadosResponse.get(0).id(),
                estadosResponse.get(0).sigla(),
                estadosResponse.get(0).nome(),
                estadosResponse.get(0).regiao().id(),
                estadosResponse.get(0).regiao().sigla(),
                estadosResponse.get(0).regiao().nome());

        final var actual = jacksonTester.parse(json);

        assertThat(actual.getObject()).isEqualTo(estadosResponse.get(0));
    }
}
