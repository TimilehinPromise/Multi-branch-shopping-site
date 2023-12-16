package com.valuemart.shop.persistence.repository;

import com.valuemart.shop.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Role findFirstByName(String name);
}
