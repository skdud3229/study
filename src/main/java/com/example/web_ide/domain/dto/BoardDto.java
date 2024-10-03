package com.example.web_ide.domain.dto;

import com.example.web_ide.domain.dao.Board;
import com.example.web_ide.domain.dao.HashTag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BoardDto(Long id, String title, String contents, LocalDateTime createdTime, LocalDateTime updatedTime, Long views, Boolean isPrivate,
                       List<HashTag> hashTags) {
    public static BoardDto from(Board board,Long views, List<HashTag> hashTags){
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .createdTime(board.getCreatedTime())
                .updatedTime(board.getUpdatedTime())
                .views(views)
                .isPrivate(board.getIsPrivate())
                .hashTags(hashTags)
                .build();
    }
}
