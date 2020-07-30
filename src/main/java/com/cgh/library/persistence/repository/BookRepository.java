package com.cgh.library.persistence.repository;

import com.cgh.library.persistence.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author cenganhui
 */
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    Page<Book> findAllByNameContaining(String name, Pageable pageable);

    Page<Book> findAllByNameContainingAndUserId(String name, Long userId, Pageable pageable);

    Page<Book> findAllByUserId(Long userId, Pageable pageable);

    Book findBookById(Long id);

}
