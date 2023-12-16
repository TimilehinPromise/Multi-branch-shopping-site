package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address>findAllByUserId(Long userId);

    Optional<Address>findByUserIdAndId(Long userId,Long addressId);

    Page<Address> findAllByUserId(Long userId, Pageable pageable);

//    Page<Address> findAllByUserId(Long userId, Pageable pageable);

}
