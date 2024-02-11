package com.valuemart.shop.persistence.repository;


import com.valuemart.shop.persistence.entity.Role;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmailAndDeletedFalse(String username);

    Boolean existsUserByEmail(String email);


    Optional<User>findByEmail(String email);

    List<User> findUsersByDeletedFalse();

    List<User>findUsersByEnabledFalse();

    List<User>findUsersByActivatedFalseAndRole(Role role);




    Page<User> findUsersByDeletedFalse(Pageable pageable);

    Optional<User> findByIdAndDeletedFalse(long userId);

    Page<User> findAllByRoleId(Long roleId, Pageable pageable);

}
