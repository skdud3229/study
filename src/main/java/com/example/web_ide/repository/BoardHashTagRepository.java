package com.example.web_ide.repository;

import com.example.web_ide.domain.dao.BoardHashTag;
import com.example.web_ide.domain.dao.HashTag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardHashTagRepository extends JpaRepository<BoardHashTag, Long> {
    Optional<BoardHashTag> findByBoardIdAndHashTagId(Long boardId, Long hashTagId);
    List<BoardHashTag> findAllByHashTagId(Long hashTagId);
    List<BoardHashTag> findAllByBoardId(Long boardId);

    @Query("select ht from BoardHashTag bht join HashTag ht on bht.hashTagId=ht.id where bht.boardId=:boardId")
    List<HashTag> findHashTagsByBoardId(@Param("boardId") Long boardId);

    @Query("delete from BoardHashTag bht where bht.boardId=:boardId")
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    void deleteAllByBoardId(Long boardId);



}
