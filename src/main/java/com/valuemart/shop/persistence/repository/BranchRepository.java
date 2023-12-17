package com.valuemart.shop.persistence.repository;


import com.valuemart.shop.persistence.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
