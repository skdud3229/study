package com.example.web_ide;

import com.example.web_ide.domain.dao.BoardHashTag;
import com.example.web_ide.domain.dao.HashTag;
import com.example.web_ide.domain.dto.BoardDto;
import com.example.web_ide.domain.viewmodel.BoardRequest;
import com.example.web_ide.repository.BoardHashTagRepository;
import com.example.web_ide.repository.BoardRepository;
import com.example.web_ide.repository.HashTagRepository;
import com.example.web_ide.service.BoardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
db는 성능이 좋다..! 이렇게 성능을 측정하기는 어렵다.
 */
@Import(ApplicationConfig.class)
@SpringBootTest
@Transactional
public class BoardHashTagTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private BoardHashTagRepository boardHashTagRepository;


    @Autowired
    private BoardService boardService;

    @PersistenceContext
    private EntityManager entityManager;



    @Test
    public void testCreateBoard() {
        BoardDto boardDto=createDefaultBoardWithHashTag();
        Assertions.assertThat(boardRepository.findById(boardDto.id())).isPresent();
        boardDto.hashTags().forEach(
                (hashTag)->{
                    HashTag hashTag2 = hashTagRepository.findByTagName(hashTag.getTagName())
                            .orElseThrow();
                    BoardHashTag boardHashTag=boardHashTagRepository.findByBoardIdAndHashTagId(boardDto.id(),hashTag.getId()).orElseThrow();
                });
    }

    @Test
    public void testDuplicateHashTag() {
        BoardDto boardDto=createDefaultBoardWithHashTag();
        BoardDto boardDto2=createDefaultBoardWithHashTag();
        boardDto.hashTags().forEach(
                (hashTag)->{
                    HashTag hashTag2=hashTagRepository.findByTagName(hashTag.getTagName()).orElseThrow();
                    List<BoardHashTag> boardHashTags=boardHashTagRepository.findAllByHashTagId(hashTag.getId());
                    Assertions.assertThat(boardHashTags.size()).isEqualTo(2);
                });
    }
    @Test
    void deleteBoardWithHashTag(){
        BoardDto boardDto=createDefaultBoardWithHashTag();
        boardService.deleteBoard(boardDto.id());
        boardRepository.findById(boardDto.id()).ifPresent((board)->{
            throw new RuntimeException("board should be deleted");
        });
        Assertions.assertThat(boardHashTagRepository.findAllByBoardId(boardDto.id()).size()).isEqualTo(0);
    }

    private BoardDto createDefaultBoardWithHashTag(){
        List<String> hashTags = List.of("first_hashTag","second_hashTag","third_hashTag");
        BoardRequest boardRequest=BoardRequest.builder()
                .title("title")
                .contents("content")
                .hashTags(hashTags)
                .build();
        return boardService.createBoard(boardRequest);
    }

    @BeforeEach
    void setUp() {
        hashTagRepository.deleteAllInBatch();
        boardHashTagRepository.deleteAllInBatch();
    }


}
