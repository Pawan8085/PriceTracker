package com.app.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PageInfoResponse {

    private int currentPage;
    private int totalPages;
    private long totalRecords;
    private int recordPerPage;
}
