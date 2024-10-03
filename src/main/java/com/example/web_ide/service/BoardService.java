package com.example.web_ide.service;

import com.example.web_ide.domain.dao.BoardHashTag;
import com.example.web_ide.domain.dao.HashTag;
import com.example.web_ide.domain.dto.BoardDto;
import com.example.web_ide.domain.viewmodel.BoardRequest;
import com.example.web_ide.domain.dao.Board;
import com.example.web_ide.exception.BoardNotFoundException;
import com.example.web_ide.repository.BoardHashTagRepository;
import com.example.web_ide.repository.BoardRepository;
import com.example.web_ide.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final ViewLikesService viewLikesService;
    private final HashTagRepository hashTagRepository;
    private final BoardHashTagRepository boardHashTagRepository;
    private final JdbcTemplate jdbcTemplate;


    public Page<Board> getPublicBoardPage(int page, int size) {
        return boardRepository.findAllByIsPrivate(false,PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public BoardDto getBoard(Long id, String ip) {
        Board board=boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        Long views=viewLikesService.addViewCount(board,ip);
        List<HashTag> hashTags=boardHashTagRepository.findHashTagsByBoardId(id);
        return BoardDto.from(board,views,hashTags);
    }

    public BoardDto createBoard(BoardRequest boardRequest) {
        Board board=boardRepository.save(boardRequest.toBoardEntity());
        List<String> tags=boardRequest.hashTags();
        if(tags.isEmpty())  return BoardDto.from(board,0L, List.of());
        List<HashTag> hashTags=insertIgnoreHashTag(tags);
        bulkMapBoardHashTags(board,hashTags);
        return BoardDto.from(board,0L,hashTags);
    }

    private List<HashTag> findOrCreateHashTags(List<String> tags){
        return tags.stream().map((tag)->
            hashTagRepository.findByTagName(tag)
                    .orElseGet(()->hashTagRepository.save(new HashTag(null,tag)))
            ).toList();
    }

    private void mapBoardHashTags(Board board,List<HashTag> hashTags){
        List<BoardHashTag> boardHashTags=hashTags.stream().map(hashTag->BoardHashTag.builder()
                .boardId(board.getId())
                .hashTagId(hashTag.getId())
                .build()).toList();
        boardHashTagRepository.saveAll(boardHashTags);
    }


    private List<HashTag> insertIgnoreHashTag(List<String> tags){
        String sql="INSERT IGNORE INTO HASH_TAG(TAG_NAME) values(?)";
        List<Object[]> batchArgs=tags.stream().map(tag->new Object[]{tag}).toList();
        jdbcTemplate.batchUpdate(sql,batchArgs);
        return hashTagRepository.findAllByTagNameIn(tags);
    }

    private void bulkMapBoardHashTags(Board board,List<HashTag> hashTags){
        String sql="INSERT INTO BOARD_HASH_TAG(BOARD_ID,HASH_TAG_ID) values(?,?)";
        List<Object[]> boardHashTags=hashTags.stream().map(hashTag->
                new Object[]{board.getId(),hashTag.getId()}).toList();
        jdbcTemplate.batchUpdate(sql,boardHashTags);
    }


    public BoardDto updateBoard(Long id,BoardRequest boardRequest) {
        Board board=boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        board.update(boardRequest.title(),boardRequest.contents());
        Long views=viewLikesService.getViewCount(id).orElse(board.getViews());
        List<HashTag> hashTags=boardHashTagRepository.findHashTagsByBoardId(id);
        return BoardDto.from(board,views,hashTags);
    }

    public void deleteBoard(Long id) {
        boardHashTagRepository.deleteAllByBoardId(id);
        boardRepository.deleteById(id);
    }
}
