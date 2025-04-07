package com.example.capstone_backend.model.auction;

import com.example.capstone_backend.model.product.Product;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

@Data
@Builder
@Jacksonized
public class Auction {
    private Product product;
    private int minPrice;
    private Date endDate;
}
