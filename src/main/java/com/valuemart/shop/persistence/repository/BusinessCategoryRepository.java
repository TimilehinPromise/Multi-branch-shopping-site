package com.valuemart.shop.persistence.repository;


import com.valuemart.shop.persistence.entity.BusinessCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessCategoryRepository extends JpaRepository<BusinessCategory,Long> {

    Optional<BusinessCategory>findByName(String name);
    Optional<BusinessCategory>findFirstById(Long id);

    List<BusinessCategory> findAllByDeletedFalse();
}
