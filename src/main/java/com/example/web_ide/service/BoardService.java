package com.example.web_ide.service;

import com.example.web_ide.dao.Board;
import com.example.web_ide.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }
}
