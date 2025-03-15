package br.com.ibge.cad.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultRecuperaProxy {

    @JsonProperty("recuperaInternalError") // Caso o JSON tenha um campo com esse nome
    private boolean recuperaInternalError;

    public boolean isRecuperaInternalError() {
        return recuperaInternalError;
    }

    public void setRecuperaInternalError(boolean recuperaInternalError) {
        this.recuperaInternalError = recuperaInternalError;
    }

}
