package br.com.ibge.cad.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    //private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {
        // Impede instanciação
    }

    public static String writeValueAsString(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter objeto para JSON", e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON para objeto", e);
        }
    }

}
