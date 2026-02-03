package com.app.transformer;

import com.app.dto.response.PageInfoResponse;
import com.app.model.ProductDetails;
import org.springframework.data.domain.Page;

public class PageDetailsTransformer {



    public static PageInfoResponse pageToPageInfoResponse(Page<ProductDetails> page){
        // Page To PageInfo
        return PageInfoResponse.builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .recordPerPage(page.getSize())
                .totalRecords(page.getTotalElements())
                .build();
    }
}
