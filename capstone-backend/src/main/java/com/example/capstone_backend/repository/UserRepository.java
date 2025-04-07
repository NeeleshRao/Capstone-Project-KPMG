package com.example.capstone_backend.repository;

import com.example.capstone_backend.DTO.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface UserRepository  extends JpaRepository<UserDto, Long> {

    Optional<UserDto> findByEmail(String email);
}
