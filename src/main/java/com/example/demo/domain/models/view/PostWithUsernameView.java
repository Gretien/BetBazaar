package com.example.demo.domain.models.view;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWithUsernameView {
    private String id;
    private String text;
    private String username;
}
