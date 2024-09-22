package org.sitecenter.common.web;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * A generic wrapper class representing a paged response.
 *
 * @param <T> the type of content in the response
 */
public class PagedResponse<T> implements Serializable {
    private List<T> content;
    private int page;

    public PagedResponse() {
    }

    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    // Constructor that accepts Page<T>
    public PagedResponse(Page<T> pageData) {
        this.content = pageData.getContent();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
        this.last = pageData.isLast();
    }

    // Getters and setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
