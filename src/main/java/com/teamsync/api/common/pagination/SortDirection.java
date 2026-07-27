package com.teamsync.api.common.pagination;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the sort direction.")
public enum SortDirection {

  @Schema(description = "Ascending sort direction")
  ASC,

  @Schema(description = "Descending sort direction")
  DESC

}
