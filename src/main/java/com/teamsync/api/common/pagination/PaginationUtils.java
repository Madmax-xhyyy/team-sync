package com.teamsync.api.common.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtils {

  private PaginationUtils() {
  }

  public static Pageable toPageable(
      PaginationRequest pagination) {

    Sort.Direction direction = pagination.directionOrDefault() == SortDirection.ASC
        ? Sort.Direction.ASC
        : Sort.Direction.DESC;

    return PageRequest.of(
        pagination.pageOrDefault(),
        pagination.sizeOrDefault(),
        Sort.by(
            direction,
            pagination.sortByOrDefault()));

  }

}
