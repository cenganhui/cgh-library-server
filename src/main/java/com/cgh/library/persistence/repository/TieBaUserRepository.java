package com.cgh.library.persistence.repository;

import com.cgh.library.persistence.entity.TieBaUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author cenganhui
 */
public interface TieBaUserRepository extends JpaRepository<TieBaUser, Long>, JpaSpecificationExecutor<TieBaUser> {

    TieBaUser findTieBaUserByOpenUid(Long openUid);

    TieBaUser findTieBaUserByUserId(Long userId);

    Page<TieBaUser> findAllByNickNameContaining(String nickName, Pageable pageable);

}
