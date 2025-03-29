package br.com.ibge.cad.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

public record MunicipioDTO(
        int id,
        String nome,
        Microrregiao microrregiao,
        @JsonAlias("regiao-imediata")
        RegiaoImediata regiaoImediata

) {
    public record Microrregiao(
            int id,
            String nome,
            Mesorregiao mesorregiao
    ) {
        public record Mesorregiao(
                int id,
                String nome,
                @JsonAlias("UF")
                UF uf
        ) {
            public record UF(
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
    }

    public record RegiaoImediata(
            int id,
            String nome,
            @JsonAlias("regiao-intermediaria")
            RegiaoIntermediaria regiaoIntermediaria
    ) {
        public record RegiaoIntermediaria(
                int id,
                String nome,
                @JsonAlias("UF")
                UF uf
        ) {
            public record UF(
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
    }
}
