package com.app.service;

import com.app.dto.request.UrlReqeust;
import com.app.dto.response.ProductDetailsResponse;
import com.app.model.ProductDetails;
import com.app.model.User;
import com.app.repository.ProductDetailsRepo;
import com.app.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {


    private EmailService emailService;
    private ProductDetailsRepo productRepo;
    private UserRepo userRepo;
    private ProductDetailServiceImpl productDetailService;
    @Autowired
    public NotificationService(EmailService emailService, ProductDetailsRepo productRepo, UserRepo userRepo, ProductDetailServiceImpl productDetailService) {
        this.emailService = emailService;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.productDetailService = productDetailService;
    }

    @Value("${app.mail}")
    private String mail;

    @Scheduled(cron = "0 0/5 * * * *")
    public void sendNotification(){

        List<User> userList = userRepo.findAll();

        for (User user : userList) {
            processUserNotification(user); // async call
        }

    }

    @Async
    public void processUserNotification(User user) {
        for (ProductDetails product : user.getProductDetails()) {
            UrlReqeust urlReqeust = new UrlReqeust();
            urlReqeust.setUrl(product.getUrl());
            ProductDetailsResponse productDetail = productDetailService.getProductDetails(urlReqeust);



            if (productDetail != null) {
                Integer currentPrice = productDetail.getCurrentPrice();
                Integer prevPrice = product.getCurrentPrice();
                Integer prevMinPrice = product.getMinPrice();
                Integer prevMaxPrice = product.getMaxPrice();

                // update previous price
                if(prevPrice != currentPrice){
                    product.setPreviousPrice(prevPrice);
                }

                // update product current price
                product.setCurrentPrice(currentPrice);

                // send notification if price has dropped
                if (currentPrice < prevPrice) {
                    String message = product.getProduct() +
                            " price has dropped from " + prevPrice + " to " + currentPrice;
                    String subject = "Price Alert";
                    product.setPriceDrop(LocalDateTime.now());

                    try {
                        emailService.send(user.getEmail(), mail, subject, message); // async
                    } catch (Exception e) {
                        System.err.println("Failed to send email: " + e.getMessage());
                    }
                }

                // update min and max price
                product.setMinPrice(Math.min(currentPrice, prevMinPrice));
                product.setMaxPrice(Math.max(currentPrice, prevMaxPrice));
            }
        }

        productRepo.saveAll(user.getProductDetails());
    }


}
