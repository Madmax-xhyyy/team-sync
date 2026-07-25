package com.teamsync.api.common.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PageQuery(

    @Min(0) Integer page,

    @Min(1) @Max(100) Integer size,

    String sortBy,

    SortDirection direction,

    String keyword

) {

  public static final int DEFAULT_PAGE = 0;
  public static final int DEFAULT_SIZE = 20;
  public static final String DEFAULT_SORT = "createdAt";

  public int pageOrDefault() {
    return page == null ? DEFAULT_PAGE : page;
  }

  public int sizeOrDefault() {
    return size == null ? DEFAULT_SIZE : size;
  }

  public String sortByOrDefault() {
    return sortBy == null || sortBy.isBlank()
        ? DEFAULT_SORT
        : sortBy;
  }

  public SortDirection directionOrDefault() {
    return direction == null
        ? SortDirection.DESC
        : direction;
  }

  public String keywordOrDefault() {
    return keyword == null
        ? ""
        : keyword.trim();
  }

}