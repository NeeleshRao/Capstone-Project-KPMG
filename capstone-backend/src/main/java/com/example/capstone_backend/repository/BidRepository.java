package com.example.capstone_backend.repository;

import com.example.capstone_backend.DTO.BidDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BidRepository extends JpaRepository<BidDto, Long> {

    List<BidDto> findByUserId(long userId);
}
