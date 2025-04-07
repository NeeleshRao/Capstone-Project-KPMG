package com.example.capstone_backend.adapter;



import com.example.capstone_backend.DTO.AuctionDto;
import com.example.capstone_backend.model.auction.AuctionDetails;
import com.example.capstone_backend.model.auction.AuctionStatus;
import com.example.capstone_backend.model.product.Product;

import java.util.Date;

public class AuctionAdapter {

    public static AuctionDetails toAuctionDetails(AuctionDto auctionDto) {
        var productDto = auctionDto.getProduct();
        var product = Product.builder().name(productDto.getName()).category(productDto.getCategory()).build();
        var status = auctionDto.getEndDate().after(new Date()) ? AuctionStatus.ACTIVE : AuctionStatus.ENDED;
        return AuctionDetails.builder().id(auctionDto.getId()).product(product).minPrice(auctionDto.getMinPrice()).endDate(auctionDto.getEndDate()).auctionStatus(status).build();
    }
}
