package com.example.capstone_backend.model.auction;

import com.example.capstone_backend.model.product.Product;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


import java.util.Date;

@Data
@Builder
@Jacksonized
public class AuctionDetails {
    private long id;
    private Product product;
    private String category;
    private int minPrice;
    private Date endDate;
    private AuctionStatus auctionStatus;
}
