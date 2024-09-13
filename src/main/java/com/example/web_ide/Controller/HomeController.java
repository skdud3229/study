package com.example.web_ide.controller;

import com.example.web_ide.dao.Board;
import com.example.web_ide.form.BoardForm;
import com.example.web_ide.repository.BoardRepository;
import com.example.web_ide.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    private final BoardService boardService;

    public HomeController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    @ResponseBody
    public String Hello(){
        return "hello!";
    }

    @PostMapping("/board")
    public ResponseEntity<Board> createBoard(@RequestBody BoardForm boardForm){
        Board board=Board.builder().title(boardForm.getTitle()).contents(boardForm.getContents()).build();
        Board response=boardService.createBoard(board);
        return ResponseEntity.ok(response);
    }
}
