package com.example.recode.repository;

import com.example.recode.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUserIdAndAddressDefault(long userId, int addressDefault);
    Optional<List<Address>> findByUserId(long userId);


}
