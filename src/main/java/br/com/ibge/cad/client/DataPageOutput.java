package br.com.ibge.cad.client;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DataPageOutput<T> {

    private List<T> data;
    private PaginationOutPut pagination;

    public record PaginationOutPut(
            int page,
            int totalPages
    ) {
        public static PaginationOutPut with(final int page, final int totalPages) {return new PaginationOutPut(page,totalPages);}

        public static PaginationOutPut empty(){return new PaginationOutPut(0,0);}
    }

    public static <T> DataPageOutput<T> with(final List<T> data,final PaginationOutPut pagination) {
        return new DataPageOutput<>(data,pagination);
    }

    public static <T> DataPageOutput<T> empty() {return new DataPageOutput<>(Collections.emptyList(),PaginationOutPut.empty());}
}
