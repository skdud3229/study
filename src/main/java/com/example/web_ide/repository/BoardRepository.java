package com.example.web_ide.repository;

import com.example.web_ide.domain.dao.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByIsPrivate(Boolean isPrivate,Pageable pageable);


}
