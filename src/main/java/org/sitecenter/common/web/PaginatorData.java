package org.sitecenter.common.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PaginatorData {
    List<Integer> pageNumbers;
    Integer page;
    Integer nextPage;
    Integer prevPage;
    Long totalElements;
    Integer totalPages;
    Integer size;

    public PaginatorData(PageResponse pageResponse) {
        if (pageResponse != null)
            initData(pageResponse.getPage(), pageResponse.getTotalElements(), pageResponse.getSize());
    }

    public PaginatorData(int page, long totalElements, int size) {
        initData(page, totalElements, size);
    }

    private void initData(int page, long totalElements, int size) {
        this.page = page;
        this.totalElements = totalElements;
        this.size = size;
        totalPages = (int) (totalElements / size) + 1;
        if (totalPages > 0) {
            int paginatorStart = Math.max(0, Math.min(page - 4, totalPages - 7));
            pageNumbers = IntStream.rangeClosed(paginatorStart, paginatorStart + Math.min(totalPages - 1, 6))
                    .boxed()
                    .collect(Collectors.toList());
            nextPage = Math.min(totalPages, page + 1);
            prevPage = Math.max(-1, page - 1);
        }
    }

}
