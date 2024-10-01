package org.sitecenter.common.web;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A class that represents the prepared pagination data set for given pagedResponse.
 * Usable in web views to simplify paginator render.
 */
@Data
@Accessors(chain = true)
public class PaginatorData implements Serializable {
    List<Integer> pageNumbers;
    Integer page;
    Integer nextPage;
    Integer prevPage;
    Long totalElements;
    Integer totalPages;
    Integer size;

    public PaginatorData() {
        pageNumbers = Collections.emptyList();
        page=0;
        nextPage=0;
        prevPage=0;
        totalPages=0;
        totalElements=0L;
        size=0;
    }

    public PaginatorData(PagedResponse pagedResponse) {
        if (pagedResponse != null)
            initData(pagedResponse.getPage(), pagedResponse.getTotalElements(), pagedResponse.getSize());
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
