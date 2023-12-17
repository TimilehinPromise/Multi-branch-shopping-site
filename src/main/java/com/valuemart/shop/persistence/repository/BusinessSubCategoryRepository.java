package com.valuemart.shop.persistence.repository;


import com.valuemart.shop.persistence.entity.BusinessSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessSubCategoryRepository extends JpaRepository<BusinessSubcategory,Long> {

    Optional<BusinessSubcategory> findByName(String name);

    Optional<BusinessSubcategory> findFirstById(Long id);

    List<BusinessSubcategory> findByDeletedFalse();
}
