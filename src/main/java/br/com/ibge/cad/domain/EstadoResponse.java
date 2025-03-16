package br.com.ibge.cad.domain;

import java.util.List;

public record EstadoResponse (
        List<Estado> data
){
    public record Estado(
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
}
