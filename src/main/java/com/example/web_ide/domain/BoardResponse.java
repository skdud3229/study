package com.example.web_ide.domain;

import com.example.web_ide.domain.dao.Board;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.web_ide.util.TimeToString.localDateTimeToString;

@Builder
public record BoardResponse(Long id, String title, String contents, String createdTime, String updatedTime){
    public static BoardResponse from(Board board){
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .createdTime(localDateTimeToString(board.getCreatedTime()))
                .updatedTime(localDateTimeToString(board.getCreatedTime()))
                .build();
    }
}
