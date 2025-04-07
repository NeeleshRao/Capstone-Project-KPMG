package com.example.capstone_backend.DTO;

import com.example.capstone_backend.model.auction.AuctionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "auction")
@Data
public class AuctionDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserDto user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "auction")
    private List<BidDto> bids;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "auction")
    private ProductDto product;

    @Column(name = "minPrice")
    private int minPrice;

    @Column(name = "endDate")
      private Date endDate;
 //   private LocalDateTime endDate;

    @Column(name = "status")
    private AuctionStatus status;

}
