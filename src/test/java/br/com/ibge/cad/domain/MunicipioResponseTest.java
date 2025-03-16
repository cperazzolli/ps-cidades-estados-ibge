package br.com.ibge.cad.domain;

import br.com.ibge.cad.DataFaker;
import br.com.ibge.cad.JacksonIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JacksonIntegrationTest
class MunicipioResponseTest {

    @Autowired
    private JacksonTester<MunicipioResponse> jacksonTester;

    @Test
    void testMarshall() throws IOException {
        final var municipioResponse = DataFaker.getMunicipios();

        final var actual = this.jacksonTester.write(municipioResponse);

        assertThat(actual)
                .hasJsonPathNumberValue("$.data[0].id",municipioResponse.data().getFirst().id())
                .hasJsonPathStringValue("$.data[0].nome",municipioResponse.data().getFirst().nome())
                .hasJsonPathNumberValue("$.data[0].microrregiao.id",municipioResponse.data().getFirst().microrregiao().id())
                .hasJsonPathStringValue("$.data[0].microrregiao.nome",municipioResponse.data().getFirst().microrregiao().nome())
                .hasJsonPathNumberValue("$.data[0].microrregiao.mesorregiao.id",municipioResponse.data().getFirst().microrregiao().mesorregiao().id())
                .hasJsonPathStringValue("$.data[0].microrregiao.mesorregiao.nome",municipioResponse.data().getFirst().microrregiao().mesorregiao().nome())
                .hasJsonPathNumberValue("$.data[0].microrregiao.mesorregiao.uf.id",municipioResponse.data().getFirst().microrregiao().mesorregiao().uf().id())
                .hasJsonPathStringValue("$.data[0].microrregiao.mesorregiao.uf.sigla",municipioResponse.data().getFirst().microrregiao().mesorregiao().uf().sigla())
                .hasJsonPathNumberValue("$.data[0].microrregiao.mesorregiao.uf.regiao.id",municipioResponse.data().getFirst().microrregiao().mesorregiao().uf().regiao().id())
                .hasJsonPathStringValue("$.data[0].microrregiao.mesorregiao.uf.regiao.nome",municipioResponse.data().getFirst().microrregiao().mesorregiao().uf().regiao().nome())
                .hasJsonPathStringValue("$.data[0].microrregiao.mesorregiao.uf.regiao.sigla",municipioResponse.data().getFirst().microrregiao().mesorregiao().uf().regiao().sigla())
                .hasJsonPathNumberValue("$.data[0].regiaoImediata.id",municipioResponse.data().getFirst().regiaoImediata().id())
                .hasJsonPathStringValue("$.data[0].regiaoImediata.nome",municipioResponse.data().getFirst().regiaoImediata().nome());
    }

}
