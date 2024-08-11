package org.roadmap.weatherapp.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginationResult<E> {

    public static final int PAGE_SIZE = 6;

    private List<E> pageContent;

    private long currentPageCount;

    private long totalCount;

    private long totalPageCount;

    public PaginationResult(List<E> pageContent, int currentPageCount, long totalCount) {
        this.pageContent = pageContent;
        this.currentPageCount = currentPageCount;
        this.totalCount = totalCount;
        this.totalPageCount = (totalCount + PAGE_SIZE - 1) / PAGE_SIZE;
    }
}
