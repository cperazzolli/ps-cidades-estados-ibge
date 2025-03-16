package br.com.ibge.cad;

import br.com.ibge.cad.domain.EstadoResponse;
import br.com.ibge.cad.domain.MunicipioResponse;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

import java.util.List;

@UtilityClass
public class DataFaker {

    private static final Faker faker = new Faker();

    public static EstadoResponse getEstados() {
        return new EstadoResponse(List.of(getEstado()));
    }

    public static EstadoResponse.Estado getEstado() {
        return new EstadoResponse.Estado(
                faker.number().numberBetween(1, 100),
                faker.options().option("SP","RJ"),
                faker.options().option("São Paulo", "Rio de Janeiro"),
                getEstadoRegiao()
        );
    }

    public static EstadoResponse.Estado.Regiao getEstadoRegiao(){
        return new EstadoResponse.Estado.Regiao(
                faker.number().numberBetween(1, 100),
                faker.options().option("S", "S"),
                faker.options().option("Sudeste","Norte")
        );
    }

    public static MunicipioResponse getMunicipios() {
       return new MunicipioResponse(List.of(getMunicipio()));
    }

    public static MunicipioResponse.Municipio getMunicipio(){
        return new MunicipioResponse.Municipio(
                faker.number().numberBetween(1, 90),
                faker.options().option("Adamantina","Adolfo","Aguaí"),
                getMunicipioRegiao(),
                getRegiaoImediata()
        );
    }

    public static MunicipioResponse.Municipio.Microrregiao getMunicipioRegiao() {
        return new MunicipioResponse.Municipio.Microrregiao(
                faker.number().numberBetween(1,100),
                faker.options().option("Adamantina","São José do Rio Preto","Pirassununga"),
                getMesorregiao()
        );
    }

    public static MunicipioResponse.Municipio.Microrregiao.Mesorregiao getMesorregiao() {
        return new MunicipioResponse.Municipio.Microrregiao.Mesorregiao(
                faker.number().numberBetween(1,100),
                faker.options().option("Presidente Prudente", "São José do Rio Preto","Campinas"),
                getMesorregiaoUF()
        );
    }

    public static MunicipioResponse.Municipio.Microrregiao.Mesorregiao.UF getMesorregiaoUF() {
        return new MunicipioResponse.Municipio.Microrregiao.Mesorregiao.UF(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo"),
                getMesorregiaoUFRegiao()
        );
    }

    public static MunicipioResponse.Municipio.Microrregiao.Mesorregiao.UF.Regiao getMesorregiaoUFRegiao() {
        return new MunicipioResponse.Municipio.Microrregiao.Mesorregiao.UF.Regiao(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo")
        );
    }

    public static MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria.UF getRegicaoIntermediariaUf() {
        return new MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria.UF(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo"),
                getUfRegiao()
        );
    }
    public static MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria.UF.Regiao getUfRegiao() {
        return new MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria.UF.Regiao(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo")
        );
    }
    public static MunicipioResponse.Municipio.RegiaoImediata getRegiaoImediata() {
        return new MunicipioResponse.Municipio.RegiaoImediata(
                faker.number().numberBetween(1, 100),
                faker.options().option("SP","RJ"),
                getRegiaoIntermediaria()
        );
    }

    public static MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria getRegiaoIntermediaria() {
        return new MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria(
                faker.number().numberBetween(1,99),
                faker.options().option("São Paulo","São Paulo"),
                getRegiaoImeditaIntermediariaUf()
        );
    }

    private static MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria.UF getRegiaoImeditaIntermediariaUf() {
        return new MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria.UF(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo"),
                getRegiaoImeditaIntermediariaUfRegiao()
        );
    }
    private static MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria.UF.Regiao getRegiaoImeditaIntermediariaUfRegiao() {
        return new MunicipioResponse.Municipio.RegiaoImediata.RegiaoIntermediaria.UF.Regiao(
                faker.number().numberBetween(1,99),
                faker.options().option("SP","SP"),
                faker.options().option("São Paulo","São Paulo")
        );
    }
}
