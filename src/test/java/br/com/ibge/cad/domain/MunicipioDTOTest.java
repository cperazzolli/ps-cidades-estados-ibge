package br.com.ibge.cad.domain;

import br.com.ibge.cad.DataFaker;
import br.com.ibge.cad.JacksonIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JacksonIntegrationTest
class MunicipioDTOTest {

    @Autowired
    private JacksonTester<MunicipioDTO> jacksonTester;

    @Test
    void testMarshall() throws IOException {
        final var municipioResponse = DataFaker.getMunicipios();

        final var actual = this.jacksonTester.write(municipioResponse);

        assertThat(actual).extractingJsonPathNumberValue("$.id").isEqualTo(municipioResponse.id());
        assertThat(actual).extractingJsonPathStringValue("$.nome").isEqualTo(municipioResponse.nome());
        assertThat(actual).extractingJsonPathNumberValue("$.microrregiao.id").isEqualTo(municipioResponse.microrregiao().id());
        assertThat(actual).extractingJsonPathStringValue("$.microrregiao.nome").isEqualTo(municipioResponse.microrregiao().nome());
        assertThat(actual).extractingJsonPathNumberValue("$.microrregiao.mesorregiao.id").isEqualTo(municipioResponse.microrregiao().mesorregiao().id());
        assertThat(actual).extractingJsonPathStringValue("$.microrregiao.mesorregiao.nome").isEqualTo(municipioResponse.microrregiao().mesorregiao().nome());
        assertThat(actual).extractingJsonPathNumberValue("$.microrregiao.mesorregiao.uf.id").isEqualTo(municipioResponse.microrregiao().mesorregiao().uf().id());
        assertThat(actual).extractingJsonPathStringValue("$.microrregiao.mesorregiao.uf.sigla").isEqualTo(municipioResponse.microrregiao().mesorregiao().uf().sigla());
        assertThat(actual).extractingJsonPathNumberValue("$.microrregiao.mesorregiao.uf.regiao.id").isEqualTo(municipioResponse.microrregiao().mesorregiao().uf().regiao().id());
        assertThat(actual).extractingJsonPathStringValue("$.microrregiao.mesorregiao.uf.regiao.nome").isEqualTo(municipioResponse.microrregiao().mesorregiao().uf().regiao().nome());
        assertThat(actual).extractingJsonPathStringValue("$.microrregiao.mesorregiao.uf.regiao.sigla").isEqualTo(municipioResponse.microrregiao().mesorregiao().uf().regiao().sigla());
        assertThat(actual).extractingJsonPathNumberValue("$.regiaoImediata.id").isEqualTo(municipioResponse.regiaoImediata().id());
        assertThat(actual).extractingJsonPathStringValue("$.regiaoImediata.nome").isEqualTo(municipioResponse.regiaoImediata().nome());
    }

    @Test
    void testUnMarshall() throws IOException {
        final var municipiosResponse = DataFaker.getMunicipios();

        final var json = """
                         {
                           "id": %d,
                           "nome": "%s",
                           "microrregiao": {
                             "id": %d,
                             "nome": "%s",
                             "mesorregiao": {
                               "id": %d,
                               "nome": "%s",
                               "UF": {
                                 "id": %d,
                                 "sigla": "%s",
                                 "nome": "%s",
                                 "regiao": {
                                   "id": %d,
                                   "sigla": "%s",
                                   "nome": "%s"
                                 }
                               }
                             }
                           },
                           "regiao-imediata": {
                             "id": %d,
                             "nome": "%s",
                             "regiao-intermediaria": {
                               "id": %d,
                               "nome": "%s",
                               "UF": {
                                 "id": %d,
                                 "sigla": "%s",
                                 "nome": "%s",
                                 "regiao": {
                                   "id": %d,
                                   "sigla": "%s",
                                   "nome": "%s"
                                 }
                               }
                             }
                           }
                         }
                     """.formatted(
                                   municipiosResponse.id(),
                                   municipiosResponse.nome(),

                                   municipiosResponse.microrregiao().id(),
                                   municipiosResponse.microrregiao().nome(),

                                   municipiosResponse.microrregiao().mesorregiao().id(),
                                   municipiosResponse.microrregiao().mesorregiao().nome(),

                                   municipiosResponse.microrregiao().mesorregiao().uf().id(),
                                   municipiosResponse.microrregiao().mesorregiao().uf().sigla(),
                                   municipiosResponse.microrregiao().mesorregiao().uf().nome(),

                                   municipiosResponse.microrregiao().mesorregiao().uf().regiao().id(),
                                   municipiosResponse.microrregiao().mesorregiao().uf().regiao().sigla(),
                                   municipiosResponse.microrregiao().mesorregiao().uf().regiao().nome(),

                                   municipiosResponse.regiaoImediata().id(),
                                   municipiosResponse.regiaoImediata().nome(),

                                   municipiosResponse.regiaoImediata().regiaoIntermediaria().id(),
                                   municipiosResponse.regiaoImediata().regiaoIntermediaria().nome(),

                                   municipiosResponse.regiaoImediata().regiaoIntermediaria().uf().id(),
                                   municipiosResponse.regiaoImediata().regiaoIntermediaria().uf().sigla(),
                                   municipiosResponse.regiaoImediata().regiaoIntermediaria().uf().nome(),

                                   municipiosResponse.regiaoImediata().regiaoIntermediaria().uf().regiao().id(),
                                   municipiosResponse.regiaoImediata().regiaoIntermediaria().uf().regiao().sigla(),
                                   municipiosResponse.regiaoImediata().regiaoIntermediaria().uf().regiao().nome());

        final var actual = jacksonTester.parse(json);

        assertThat(actual).isEqualTo(municipiosResponse);
    }

}
