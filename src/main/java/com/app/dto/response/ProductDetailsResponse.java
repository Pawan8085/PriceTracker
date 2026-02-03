package com.app.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDetailsResponse {

    private Long productId;
    private String url;
    private String image;
    private  String product;
    private Integer previousPrice;
    private Integer currentPrice;
    private Integer minPrice;
    private  Integer maxPrice;
    private LocalDateTime price_drop;
}
