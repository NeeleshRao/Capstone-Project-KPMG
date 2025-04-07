package com.example.capstone_backend.repository;

import com.example.capstone_backend.DTO.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProductRepository extends JpaRepository<ProductDto, Long> {
}
