package com.app.repository;

import com.app.model.ProductDetails;
import com.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT p FROM User u JOIN u.productDetails p WHERE u.email = :email")
    Page<ProductDetails> findProductDetailsByEmail(@Param("email") String email, Pageable pageable);

}
