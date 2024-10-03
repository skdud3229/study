package com.example.web_ide.domain.viewmodel;
import com.example.web_ide.domain.dao.Board;
import lombok.Builder;

import static com.example.web_ide.util.TimeToString.localDateTimeToString;

@Builder
public record BoardSummary(Long id, String title, String createdTime){
    public static BoardSummary from(Board board) {
        return BoardSummary.builder()
                .id(board.getId())
                .title(board.getTitle())
                .createdTime(localDateTimeToString(board.getCreatedTime()))
                .build();
    }
}






