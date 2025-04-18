package com.example.capstone_backend.model.bid;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Bid {
    private long userId;
    private long price;
}
