package com.app.service;

import com.app.dto.request.UrlReqeust;
import com.app.dto.response.ApiResponse;
import com.app.dto.response.PageInfoResponse;
import com.app.dto.response.ProductDetailsResponse;
import com.app.exception.ProductException;
import com.app.exception.UserException;
import com.app.model.ProductDetails;
import com.app.model.User;
import com.app.repository.ProductDetailsRepo;
import com.app.repository.UserRepo;
import com.app.transformer.PageDetailsTransformer;
import com.app.transformer.ProductDetailsTransformer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDetailServiceImpl implements  ProductDetailService{


    private final ProductDetailsRepo productDetailsRepo;
    private final UserRepo userRepo;

    @Autowired
    public ProductDetailServiceImpl(ProductDetailsRepo productDetailsRepo, UserRepo userRepo) {
        this.productDetailsRepo = productDetailsRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void addProductDetails(UrlReqeust urlReqeust) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(username).orElseThrow(() -> new UserException("User not found!"));

        ProductDetailsResponse productDetailsResponse = getProductDetails(urlReqeust);
        int currentPrice = productDetailsResponse.getCurrentPrice();
        productDetailsResponse.setPreviousPrice(currentPrice);
        productDetailsResponse.setMaxPrice(currentPrice);
        productDetailsResponse.setMinPrice(currentPrice);

        ProductDetails productDetails = ProductDetailsTransformer.productDetailsResponseToProductDetails(productDetailsResponse);
        user.getProductDetails().add(productDetails);
        userRepo.save(user);


    }

    @Override
    public void removeProductDetails(Long productId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(username).orElseThrow(() -> new UserException("User not found!"));

        int deleted = productDetailsRepo.deleteUserProduct(user.getUserId(), productId);
        if(deleted == 0){
            throw  new ProductException("Product not found or not owned by user");
        }

    }

    ProductDetailsResponse getProductDetails(UrlReqeust urlReqeust){

        try{

            String url = urlReqeust.getUrl();

            Document document = Jsoup.connect(url)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                    "Chrome/120.0.0.0 Safari/537.36"
                    )
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate")
                    .referrer("https://www.google.com")
                    .timeout(15000)
                    .get();


            Element titleEl = document.selectFirst("#productTitle");
            String title = titleEl != null ? titleEl.text().trim() : null;
            Element priceEl = document.selectFirst(".a-price-whole");
            String price = priceEl != null ? priceEl.text().trim() : null;

            Element urlEl = document.selectFirst("#landingImage");
            String image = null;
            if (urlEl != null) {
                image = urlEl.attr("src").trim();

            }

            StringBuilder priceStr = new StringBuilder();
            for(char c : price.toCharArray()){

                if(Character.isDigit(c)){
                    priceStr.append(c);
                }
            }

            int productPrice = Integer.parseInt(priceStr.toString());


           return ProductDetailsResponse.builder()
                    .product(title)
                    .url(url)
                   .image(image)
                    .currentPrice(productPrice)
                    .build();



        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
    @Override
    public ApiResponse<ProductDetailsResponse> findAllProductDetails(int page_no) {

        // get the username
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // create page object
        Pageable pageable = PageRequest.of(page_no, 9);
        Page<ProductDetails> data = userRepo.findProductDetailsByEmail(username, pageable);

        ApiResponse<ProductDetailsResponse> response = new ApiResponse<>();
        List<ProductDetailsResponse> products = new ArrayList<>();

        // convert entity to dto
        for(ProductDetails productDetails : data.getContent()){
            products.add(ProductDetailsTransformer.productDetailsToProductDetailsResponse(productDetails));
        }

        // set product & page information
        response.setData(products);
        PageInfoResponse pageInfoResponse = PageDetailsTransformer.pageToPageInfoResponse(data);
        response.setPageInfo(pageInfoResponse);

        return response;
    }

    @Override
    public ApiResponse<ProductDetailsResponse> searchProducts(String key, int page_no) {
        // create page object
        Pageable pageable = PageRequest.of(page_no, 9);
        Page<ProductDetails> data = productDetailsRepo.findByProductName(key, pageable);

        ApiResponse<ProductDetailsResponse> response = new ApiResponse<>();
        List<ProductDetailsResponse> products = new ArrayList<>();

        // convert entity to dto
        for(ProductDetails productDetails : data.getContent()){
            products.add(ProductDetailsTransformer.productDetailsToProductDetailsResponse(productDetails));
        }

        // set product & page information
        response.setData(products);
        PageInfoResponse pageInfoResponse = PageDetailsTransformer.pageToPageInfoResponse(data);
        response.setPageInfo(pageInfoResponse);

        return response;
    }

    @Override
    public ApiResponse<ProductDetailsResponse> getPricedDroppedProduct(int page_no) {

        // create page object
        Pageable pageable = PageRequest.of(page_no, 9);
        Page<ProductDetails> data = productDetailsRepo.findProductByPriceDrop(pageable);

        ApiResponse<ProductDetailsResponse> response = new ApiResponse<>();
        List<ProductDetailsResponse> products = new ArrayList<>();

        // convert entity to dto
        for(ProductDetails productDetails : data.getContent()){
            products.add(ProductDetailsTransformer.productDetailsToProductDetailsResponse(productDetails));
        }

        // set product & page information
        response.setData(products);
        PageInfoResponse pageInfoResponse = PageDetailsTransformer.pageToPageInfoResponse(data);
        response.setPageInfo(pageInfoResponse);

        return response;
    }
}
