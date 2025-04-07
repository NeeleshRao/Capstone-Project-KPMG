package com.example.capstone_backend.controller;

import com.example.capstone_backend.DTO.AuctionDto;
import com.example.capstone_backend.DTO.BidDto;
import com.example.capstone_backend.exception.ResourceNotFoundException;
import com.example.capstone_backend.model.auction.AuctionStatus;
import com.example.capstone_backend.model.bid.Bid;
import com.example.capstone_backend.repository.AuctionRepository;
import com.example.capstone_backend.repository.BidRepository;
import com.example.capstone_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;

import static com.example.capstone_backend.application.ApplicationProperties.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/bids")
@CrossOrigin
public class BidController {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Autowired
    public BidController(UserRepository userRepository, AuctionRepository auctionRepository, BidRepository bidRepository) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    @PostMapping("/{auctionId}")
    public ResponseEntity<?> createBid(@PathVariable long auctionId, @RequestBody Bid bid) {
        // Retrieve the auction details; throw an exception if not found.
        AuctionDto auctionDto = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        // Convert auction end date (java.util.Date) to LocalDateTime for correct comparison.
        LocalDateTime auctionEndDateTime = LocalDateTime.ofInstant(auctionDto.getEndDate().toInstant(), ZoneId.systemDefault());

        // Check if the auction has expired.
        if (!LocalDateTime.now().isBefore(auctionEndDateTime)) {
            // Auction has ended; update status if it's not already set to ENDED.
            if (auctionDto.getStatus() != AuctionStatus.ENDED) {
                auctionDto.setStatus(AuctionStatus.ENDED);

                // Awarding logic: if there are any bids, award the auction to the highest bid.
                if (auctionDto.getBids() != null && !auctionDto.getBids().isEmpty()) {
                    BidDto highestBid = auctionDto.getBids()
                            .stream()
                            .max(Comparator.comparing(BidDto::getPrice))
                            .orElse(null);
                    if (highestBid != null) {
                        // NOTE: AuctionDto does not currently have a winner field.
                        // If you wish to store the awarded user,
                        // consider adding a field like "private UserDto winner;" in AuctionDto.
                        // Then you could call: auctionDto.setWinner(highestBid.getUser());
                        System.out.println("Auction ended. Winner: " + highestBid.getUser().getId());
                    }
                }
                auctionRepository.save(auctionDto);
            }
            return ResponseEntity.badRequest().body("Auction has ended. No further bids are accepted.");
        }

        // Validate: bid amount must be at least the auction's minimum price.
        if (bid.getPrice() < auctionDto.getMinPrice()) {
            return ResponseEntity.badRequest().body("Bid amount is lower than the auction's minimum price.");
        }

        // Validate: preventing the auction's owner from bidding on their own auction.
        if (bid.getUserId() == auctionDto.getUser().getId()) {
            return ResponseEntity.badRequest().body("Auction owner cannot place a bid.");
        }

        // Ensure the bidding user exists.
        var userDto = userRepository.findById(bid.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create and fill the BidDto.
        BidDto bidDto = new BidDto();
        bidDto.setUser(userDto);
        bidDto.setAuction(auctionDto);
        bidDto.setPrice(bid.getPrice());
        bidDto = bidRepository.save(bidDto);

        // Return a CREATED response with the new bid id.
        return ResponseEntity.created(URI.create(BASE_URL + "/bids/" + bidDto.getId())).body(bidDto.getId());
    }
}
