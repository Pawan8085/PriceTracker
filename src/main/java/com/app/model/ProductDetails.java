package com.app.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private  String product;
    @Column(length = 1500)
    private  String url;
    private String image;
    private Integer previousPrice;
    private Integer currentPrice;
    private Integer minPrice;
    private  Integer maxPrice;
    private LocalDateTime priceDrop;

}
