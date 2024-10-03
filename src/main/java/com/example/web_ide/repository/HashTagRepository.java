package com.example.web_ide.repository;

import com.example.web_ide.domain.dao.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByTagName(String tagName);
    List<HashTag> findAllByTagNameIn(List<String> tagNames);
}
