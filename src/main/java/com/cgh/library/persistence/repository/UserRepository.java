package com.cgh.library.persistence.repository;

import com.cgh.library.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotBlank;

/**
 * @author cenganhui
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Page<User> findAllByUsernameContaining(@NotBlank String username, Pageable pageable);

    User findUserById(Long id);

    User findUserByUsernameAndPassword(String username, String password);

    User findUserByUsername(String username);

}
