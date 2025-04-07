package com.example.capstone_backend.model.product;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Product {
    private String name;
    private Category category;
}
