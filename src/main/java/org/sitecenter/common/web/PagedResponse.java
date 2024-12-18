package org.sitecenter.common.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A generic wrapper class representing a paged response.
 *
 * @param <T> the type of content in the response
 */
public class PagedResponse<T> implements Serializable {
    private List<T> content;
    private int page;

    private Map<String, Object> payload = new HashMap<>();

    public PagedResponse() {
    }

    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PagedResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int)totalElements/size;
        this.last = page <= totalPages;
    }

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = page <= totalPages;
    }


    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean isLast) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = isLast;
    }

    //
//    // Constructor that accepts Page<T>
//    public PagedResponse(Page<T> pageData) {
//        this.content = pageData.getContent();
//        this.page = pageData.getNumber();
//        this.size = pageData.getSize();
//        this.totalElements = pageData.getTotalElements();
//        this.totalPages = pageData.getTotalPages();
//        this.last = pageData.isLast();
//    }
//
//    public PagedResponse(final List<T> content, final long totalElements, final Pageable pageable) {
//        this.content = content;
//        this.page = pageable.getPageNumber();
//        this.size = pageable.getPageSize();
//        this.totalElements = totalElements;
//        this.totalPages = 1;
//        this.last = true;
//    }

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

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
