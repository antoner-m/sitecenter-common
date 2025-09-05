package org.sitecenter.common.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A generic wrapper class representing a paged response.
 *
 * page index start from 0
 *
 * @param <T> the type of content in the response
 */
public class PagedResponse<T> implements Serializable {
    private List<T> content;
    private int page;

    private Map<String, Object> payload = new HashMap<>();

    public PagedResponse() {
        content = new ArrayList<>();
    }

    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;

    public PagedResponse(List<T> fullList, int page, int size) {
        if (fullList != null) {
            int fromIdx = page * size;
            int toIdx = fromIdx + size;
            if (fromIdx >= 0 && fromIdx < fullList.size()) {
                if (toIdx > fullList.size())
                    toIdx = fullList.size();
                this.content = fullList.subList(fromIdx, toIdx);
                this.totalElements = fullList.size();
                this.page = page;
                this.size = size;
            } else {
                this.page = 0;
                this.size = 0;
                this.content = new ArrayList<>();
                this.totalElements = fullList.size();
            }
        } else {
            this.page = 0;
            this.size = 0;
            this.totalPages = 0;
        }
        this.totalPages = calcTotalPages();
        this.last = page >= totalPages-1;
        this.first = page == 0;
    }

    public PagedResponse(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = calcTotalPages();
        this.last = page >= totalPages-1;
        this.first = page == 0;
    }

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = page >= totalPages-1;
        this.first = page == 0;
    }

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean isLast) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = isLast;
        this.first = page == 0;
    }

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean isLast, boolean isFirst) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = isLast;
        this.first = isFirst;
    }

    private int calcTotalPages() {
        int result = 0;
        if (size > 0) {
            result = (int) Math.ceil((double) totalElements / size);
        }
        return result;
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

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}