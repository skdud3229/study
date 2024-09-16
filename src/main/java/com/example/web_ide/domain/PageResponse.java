package com.example.web_ide.domain;

import lombok.Builder;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;

@Builder
public record PageResponse<T>(List<T> dataList, PageInfo pageInfo) {
    public static <T,D> PageResponse<T> from(Page<D> originPage, Function<D,T> converter){
        return PageResponse.<T>builder()
                .dataList(originPage.getContent().stream()
                        .map(converter)
                        .toList())
                .pageInfo(PageInfo.from(originPage))
                .build();
    }
}
