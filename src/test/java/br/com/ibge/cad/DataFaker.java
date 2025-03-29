package br.com.ibge.cad;

import br.com.ibge.cad.domain.EstadoDTO;
import br.com.ibge.cad.domain.MunicipioDTO;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class DataFaker {

    private static final Faker faker = new Faker();

    public static List<EstadoDTO> getEstados(int quantidade) {
        return IntStream.range(0, quantidade)
                .mapToObj(i -> getEstado())
                .collect(Collectors.toList());
    }

    public static EstadoDTO getEstado() {

        var estados = Map.of(
                "SP", "São Paulo",
                "RJ", "Rio de Janeiro",
                "SC", "Santa Catarina",
                "Go", "Goias"
        );

        var sigla = faker.options().option(estados.keySet().toArray(new String[0]));
        var nome = estados.get(sigla);

        return new EstadoDTO(
                faker.number().numberBetween(1, 100),
                sigla,
                nome,
                getEstadoRegiao()
        );
    }



    public static EstadoDTO.Regiao getEstadoRegiao(){
        return new EstadoDTO.Regiao(
                faker.number().numberBetween(1, 100),
                faker.options().option("S", "S"),
                faker.options().option("Sudeste","Norte")
        );
    }

    public static MunicipioDTO getMunicipios() {
        return new MunicipioDTO(
                faker.number().numberBetween(1, 90),
                faker.options().option("Adamantina","Adolfo","Aguaí"),
                getMunicipioRegiao(),
                getRegiaoImediata()
        );
    }


    public static MunicipioDTO.Microrregiao getMunicipioRegiao() {
        return new MunicipioDTO.Microrregiao(
                faker.number().numberBetween(1,100),
                faker.options().option("Adamantina","São José do Rio Preto","Pirassununga"),
                getMesorregiao()
        );
    }

    public static MunicipioDTO.Microrregiao.Mesorregiao getMesorregiao() {
        return new MunicipioDTO.Microrregiao.Mesorregiao(
                faker.number().numberBetween(1,100),
                faker.options().option("Presidente Prudente", "São José do Rio Preto","Campinas"),
                getMesorregiaoUF()
        );
    }

    public static MunicipioDTO.Microrregiao.Mesorregiao.UF getMesorregiaoUF() {
        return new MunicipioDTO.Microrregiao.Mesorregiao.UF(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo"),
                getMesorregiaoUFRegiao()
        );
    }

    public static MunicipioDTO.Microrregiao.Mesorregiao.UF.Regiao getMesorregiaoUFRegiao() {
        return new MunicipioDTO.Microrregiao.Mesorregiao.UF.Regiao(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo")
        );
    }

    public static MunicipioDTO.RegiaoImediata.RegiaoIntermediaria.UF getRegicaoIntermediariaUf() {
        return new MunicipioDTO.RegiaoImediata.RegiaoIntermediaria.UF(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo"),
                getUfRegiao()
        );
    }
    public static MunicipioDTO.RegiaoImediata.RegiaoIntermediaria.UF.Regiao getUfRegiao() {
        return new MunicipioDTO.RegiaoImediata.RegiaoIntermediaria.UF.Regiao(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo")
        );
    }
    public static MunicipioDTO.RegiaoImediata getRegiaoImediata() {
        return new MunicipioDTO.RegiaoImediata(
                faker.number().numberBetween(1, 100),
                faker.options().option("SP","RJ"),
                getRegiaoIntermediaria()
        );
    }

    public static MunicipioDTO.RegiaoImediata.RegiaoIntermediaria getRegiaoIntermediaria() {
        return new MunicipioDTO.RegiaoImediata.RegiaoIntermediaria(
                faker.number().numberBetween(1,99),
                faker.options().option("São Paulo","São Paulo"),
                getRegiaoImeditaIntermediariaUf()
        );
    }

    private static MunicipioDTO.RegiaoImediata.RegiaoIntermediaria.UF getRegiaoImeditaIntermediariaUf() {
        return new MunicipioDTO.RegiaoImediata.RegiaoIntermediaria.UF(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo"),
                getRegiaoImeditaIntermediariaUfRegiao()
        );
    }
    private static MunicipioDTO.RegiaoImediata.RegiaoIntermediaria.UF.Regiao getRegiaoImeditaIntermediariaUfRegiao() {
        return new MunicipioDTO.RegiaoImediata.RegiaoIntermediaria.UF.Regiao(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo")
        );
    }
}
