package com.example.web_ide.domain;
import com.example.web_ide.domain.dao.Board;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record BoardRequest(@NotBlank(message="게시글의 제목이 비어있습니다.")
                           @Length(max=100,message="게시글의 제목은 최대 {max}자입니다.")
                           String title,

                           @NotBlank(message="게시글의 내용이 비어있습니다.")
                           @Length(max=1000,message="게시글의 내용은 최대 {max}자입니다.")
                           String contents) {
    public Board toEntity() {
        return Board.builder()
                .title(this.title())
                .contents(this.contents())
                .build();
    }
}
