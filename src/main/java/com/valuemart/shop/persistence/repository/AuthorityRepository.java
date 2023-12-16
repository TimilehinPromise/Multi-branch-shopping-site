package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
