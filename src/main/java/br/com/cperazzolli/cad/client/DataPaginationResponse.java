package br.com.cperazzolli.cad.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DataPaginationResponse<T> {

    private List<T> data;
    private PaginationResponse pagination;

    public record PaginationResponse(
            int page,
            int totalPages
    ) {
        public static PaginationResponse with(final int page, final int totalPages) {return new PaginationResponse(page,totalPages);}

        public static PaginationResponse empty(){return new PaginationResponse(0,0);}

        @JsonIgnore
        public boolean hasNext() {
            return Objects.nonNull(this.page)
                 && Objects.nonNull(this.totalPages)
                 && this.page < this.totalPages;
        }

    }
    public static <T> DataPaginationResponse<T> with(final List<T> data,final PaginationResponse pagination) {
        return new DataPaginationResponse<>(data,pagination);
    }

    public static <T> DataPaginationResponse<T> empty() {return new DataPaginationResponse<>(Collections.emptyList(), PaginationResponse.empty());}

    @JsonIgnore
    public boolean hasNext() {
        return Objects.nonNull(this.pagination)
                && this.pagination.hasNext();
    }

    @JsonIgnore
    public boolean hasrror() {
        return Objects.nonNull(this.data)
                || this.data.isEmpty();
    }

}
