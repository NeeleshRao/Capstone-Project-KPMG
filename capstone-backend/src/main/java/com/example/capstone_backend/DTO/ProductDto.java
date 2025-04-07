package com.example.capstone_backend.DTO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import com.example.capstone_backend.model.product.Category;

@Entity
@Table(name = "product")
@Data
public class ProductDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auction_id")
    @ToString.Exclude
    private AuctionDto auction;

    @Column(name = "category")
    private Category category;


    @Column(name = "name")
    private String name;

//    @Column(name = "photo")
//    private String photo;
}
