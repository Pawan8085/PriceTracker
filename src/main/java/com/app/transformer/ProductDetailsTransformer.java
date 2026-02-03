package com.app.transformer;

import com.app.dto.response.ProductDetailsResponse;
import com.app.model.ProductDetails;

public class ProductDetailsTransformer {


    public static ProductDetailsResponse productDetailsToProductDetailsResponse(ProductDetails productDetails){
        // ProductDetails To ProductDetailsResponse
        return  ProductDetailsResponse.builder()
                .productId(productDetails.getProductId())
                .image(productDetails.getImage())
                .url(productDetails.getUrl())
                .product(productDetails.getProduct())
                .previousPrice(productDetails.getPreviousPrice())
                .currentPrice(productDetails.getCurrentPrice())
                .maxPrice(productDetails.getMaxPrice())
                .minPrice(productDetails.getMinPrice())
                .price_drop(productDetails.getPriceDrop())
                .build();
    }


    public static ProductDetails productDetailsResponseToProductDetails(ProductDetailsResponse productDetailsResponse){
        // ProductDetailsResponse to ProductDetails
        return ProductDetails.builder()
                .product(productDetailsResponse.getProduct())
                .url(productDetailsResponse.getUrl())
                .image(productDetailsResponse.getImage())
                .previousPrice(productDetailsResponse.getPreviousPrice())
                .currentPrice(productDetailsResponse.getCurrentPrice())
                .minPrice(productDetailsResponse.getMinPrice())
                .maxPrice(productDetailsResponse.getMaxPrice())
                .priceDrop(productDetailsResponse.getPrice_drop())
                .build();
    }
}
