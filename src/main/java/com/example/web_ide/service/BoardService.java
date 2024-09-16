package com.example.web_ide.service;

import com.example.web_ide.domain.BoardRequest;
import com.example.web_ide.domain.dao.Board;
import com.example.web_ide.exception.BoardNotFoundException;
import com.example.web_ide.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<Board> getBoardPage(int page, int size) {
        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
    }
    public Board createBoard(BoardRequest boardRequest) {
        return boardRepository.save(boardRequest.toEntity());
    }

    public Board updateBoard(Long id,BoardRequest boardRequest) {
        Board board=boardRepository.findById(id).orElseThrow();
        board.update(boardRequest.title(),boardRequest.contents());
        return board;
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
