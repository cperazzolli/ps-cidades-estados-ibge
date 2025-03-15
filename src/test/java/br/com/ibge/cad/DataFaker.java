package br.com.ibge.cad;

import br.com.ibge.cad.domain.EstadoResponse;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

@UtilityClass
public class DataFaker {

    private static final Faker faker = new Faker();
    public static EstadoResponse getEstados() {

        return new EstadoResponse(
                faker.number().numberBetween(1, 100),
                faker.options().option("SP","RJ"),
                faker.options().option("São Paulo", "Rio de Janeiro"),
                getRegiao()
        );
    }

    public static EstadoResponse.Regiao getRegiao(){
        return new EstadoResponse.Regiao(
                faker.number().numberBetween(1, 100),
                faker.options().option("S", "S"),
                faker.options().option("Sudeste","Norte")
        );
    }
}
