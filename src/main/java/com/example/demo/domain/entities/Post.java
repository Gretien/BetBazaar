package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends BaseEntity{
    @Column(columnDefinition = "text",nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
