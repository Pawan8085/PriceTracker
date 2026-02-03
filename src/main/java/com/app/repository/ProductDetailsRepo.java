package com.app.repository;

import com.app.model.ProductDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductDetailsRepo extends JpaRepository<ProductDetails, Long> {

    @Query("Select p FROM ProductDetails p WHERE p.product LIKE CONCAT('%', :query, '%')")
    Page<ProductDetails> findByProductName(@Param("query") String query, Pageable pageable);


    @Query("SELECT p FROM ProductDetails p WHERE p.priceDrop IS NOT NULL ORDER BY p.priceDrop DESC")
    Page<ProductDetails> findProductByPriceDrop(Pageable pageable);


    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM product_details WHERE product_id = :productId AND user_id = :userId",
            nativeQuery = true
    )
    int deleteUserProduct(@Param("userId") Long userId, @Param("productId") Long productId);


}
