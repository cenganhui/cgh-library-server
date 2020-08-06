package com.cgh.library.persistence.repository;

import com.cgh.library.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;

/**
 * @author cenganhui
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Page<User> findAllByUsernameContaining(@NotBlank String username, Pageable pageable);

    User findUserById(Long id);

    User findUserByUsernameAndPassword(String username, String password);

    User findUserByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "update lib_user set admin = ?2 where id = ?1", nativeQuery = true)
    Integer updateAdminById(Long id, Boolean admin);

}
