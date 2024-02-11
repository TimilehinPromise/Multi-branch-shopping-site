package com.valuemart.shop.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentlyViewed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "view_timestamp", nullable = false)
    private LocalDateTime viewTimestamp;

    public RecentlyViewed(Long userId, Long productId, LocalDateTime viewTimestamp) {
        this.userId = userId;
        this.productId = productId;
        this.viewTimestamp = viewTimestamp;
    }

}
