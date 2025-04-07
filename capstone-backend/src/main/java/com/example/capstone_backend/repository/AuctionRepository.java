package com.example.capstone_backend.repository;

import com.example.capstone_backend.DTO.AuctionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AuctionRepository extends JpaRepository<AuctionDto, Long> {
}
