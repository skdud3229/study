package com.example.web_ide.domain.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"boardId", "hashTagId"}))
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardHashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;

    private Long hashTagId;

    @CreationTimestamp
    private LocalDateTime createdTime;
}
