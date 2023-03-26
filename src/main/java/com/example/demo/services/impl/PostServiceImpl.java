package com.example.demo.services.impl;

import com.example.demo.domain.entities.Post;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.models.binding.PostAddModel;
import com.example.demo.domain.models.view.PostWithUsernameView;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addPost(PostAddModel postAddModel, String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
        Post post = new Post(postAddModel.getText(),user);
        this.postRepository.saveAndFlush(post);
    }

    @Override
    public List<PostWithUsernameView> findAll() {
        List<Post> all = this.postRepository.findAll();
        return this.map(all);
    }

    @Override
    public void removePostById(String id) {
        this.postRepository.deleteById(id);
    }

    private List<PostWithUsernameView> map(List<Post> all) {
        return all.stream().map(post ->
                PostWithUsernameView.builder().id(post.getId())
                        .text(post.getText())
                        .username(post.getUser().getUsername())
                        .build()
        ).collect(Collectors.toList());
    }
}
