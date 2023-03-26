package com.example.demo.services;

import com.example.demo.domain.models.binding.PostAddModel;
import com.example.demo.domain.models.view.PostWithUsernameView;

import java.util.List;

public interface PostService {
    void addPost(PostAddModel postAddModel, String username);

    List<PostWithUsernameView> findAll();

    void removePostById(String id);
}
