package com.example.capstone_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.capstone_backend.adapter.AuctionAdapter;
import com.example.capstone_backend.repository.AuctionRepository;
import com.example.capstone_backend.repository.BidRepository;
import com.example.capstone_backend.repository.UserRepository;
import com.example.capstone_backend.DTO.AuctionDto;
import com.example.capstone_backend.DTO.BidDto;
import com.example.capstone_backend.exception.ResourceNotFoundException;
import com.example.capstone_backend.model.auction.AuctionDetails;
import com.example.capstone_backend.model.bid.Bid;

import java.net.URI;
import java.util.List;

import static com.example.capstone_backend.adapter.AuctionAdapter.toAuctionDetails;
import static com.example.capstone_backend.application.ApplicationProperties.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/auctions")
@CrossOrigin
public class AuctionController {

    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Autowired
    public AuctionController(UserRepository userRepository, AuctionRepository auctionRepository, BidRepository bidRepository) {
        this.userRepository = userRepository;
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    @GetMapping
    public List<AuctionDetails> getAuctions() {
        return auctionRepository.findAll()
                .stream()
                .map(AuctionAdapter::toAuctionDetails)
                .toList();
    }

    @GetMapping("/{auctionId}")
    public AuctionDetails getAuction(@PathVariable long auctionId) {
        var auctionDto = findAuctionById(auctionId);
        return toAuctionDetails(auctionDto);
    }

    @DeleteMapping("/{auctionId}")
    public void deleteAuction(@PathVariable long auctionId) {
        auctionRepository.deleteById(auctionId);
    }

    @PostMapping("/{auctionId}/bids")
    public ResponseEntity<?> createBid(@PathVariable long auctionId, @RequestBody Bid bid) {
        var userDto = userRepository.findById(bid.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var auctionDto = findAuctionById(auctionId);
        System.out.println(bid.getUserId());
        System.out.println(auctionDto.getUser().getId());
        if ((int)bid.getUserId() == auctionDto.getUser().getId()) {
            return ResponseEntity.badRequest()
                    .body("You cannot bid on your own auction.");
        }

        var bidDto = new BidDto();
        bidDto.setUser(userDto);
        bidDto.setAuction(auctionDto);
        fillBidDto(bidDto, bid);
        bidDto = bidRepository.save(bidDto);
        System.out.println("Received endDate: " + auctionDto.getEndDate());

        return ResponseEntity.created(URI.create(BASE_URL + "auctions/" + auctionId + "/bids/" + bidDto.getId())).body(bidDto.getId());
    }

    @GetMapping("/{auctionId}/bids")
    public List<Bid> getBids(@PathVariable long auctionId) {
        var auctionDto = findAuctionById(auctionId);
        return auctionDto.getBids()
                .stream()
                .map(bidDto -> Bid.builder().userId(bidDto.getUser().getId()).price(bidDto.getPrice()).build())
                .toList();
    }

    private AuctionDto findAuctionById(long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
    }

    private static void fillBidDto(BidDto bidDto, Bid bid) {
        bidDto.setPrice(bid.getPrice());
    }
}
