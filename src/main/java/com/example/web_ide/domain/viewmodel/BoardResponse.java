package com.example.web_ide.domain.viewmodel;

import com.example.web_ide.domain.dao.Board;
import com.example.web_ide.domain.dao.HashTag;
import com.example.web_ide.domain.dto.BoardDto;
import lombok.Builder;

import java.util.List;

import static com.example.web_ide.util.TimeToString.localDateTimeToString;

@Builder
public record BoardResponse(Long id, String title, String contents, String createdTime, String updatedTime, Long views,
                            List<HashTag> hashTags){
    public static BoardResponse from(BoardDto boardDto){
        return BoardResponse.builder()
                .id(boardDto.id())
                .title(boardDto.title())
                .contents(boardDto.contents())
                .createdTime(localDateTimeToString(boardDto.createdTime()))
                .updatedTime(localDateTimeToString(boardDto.updatedTime()))
                .views(boardDto.views())
                .hashTags(boardDto.hashTags())
                .build();
    }

}
