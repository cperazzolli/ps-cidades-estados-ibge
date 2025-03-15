package br.com.ibge.cad.domain;

public record EstadoResponse (
        int id,
        String sigla,
        String nome,
        Regiao regiao
) {
    public record Regiao(
            int id,
            String sigla,
            String nome
    ) {
    }
}
