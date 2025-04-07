package com.example.capstone_backend.model.bid;

import com.example.capstone_backend.model.auction.AuctionDetails;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


@Data
@Builder
@Jacksonized
public class BidDetails {
    private AuctionDetails auction;
    private long price;
}
