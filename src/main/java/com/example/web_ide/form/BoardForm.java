package com.example.web_ide.form;

import lombok.Getter;

@Getter
public class BoardForm {

    private String title;
    private String contents;
    public String getTitle() {
        return title;
    }
    public String getContents() {
        return contents;
    }
}
