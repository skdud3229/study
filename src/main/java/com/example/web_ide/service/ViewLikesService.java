package com.example.web_ide.service;

import com.example.web_ide.domain.dao.Board;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface ViewLikesService {
    long addViewCount(Board board, String identifier);
    Optional<Long> getViewCount(Long boardId);
}
