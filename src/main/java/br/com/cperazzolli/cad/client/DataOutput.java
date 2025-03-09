package br.com.cperazzolli.cad.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DataOutput<T> {

    private T data;

    public static <T> DataOutput<T> with(final T data) {
        return new DataOutput<>(data);
    }
}
