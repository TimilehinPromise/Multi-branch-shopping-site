package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.RecentlyViewed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed,Long> {

    RecentlyViewed findByUserIdAndProductId(Long userId, Long productId);

    Long countByUserId(Long userId);

    @Query("SELECT rv FROM RecentlyViewed rv WHERE rv.userId = :userId ORDER BY rv.viewTimestamp ASC")
    List<RecentlyViewed> findOldestByUserId(@Param("userId") Long userId, Pageable pageable);


    @Query("SELECT rv FROM RecentlyViewed rv WHERE rv.userId = :userId ORDER BY rv.viewTimestamp DESC")
    List<RecentlyViewed> findByUserId(@Param("userId") Long userId);

}
