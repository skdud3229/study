package com.example.web_ide.domain;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageInfo(int page, int size, int totalPage, long totalSize) {
    static PageInfo from(Page<?> originPage) {
        return PageInfo.builder()
                .page(originPage.getNumber())
                .size(originPage.getSize())
                .totalPage(originPage.getTotalPages())
                .totalSize(originPage.getTotalElements())
                .build();
    }
}
