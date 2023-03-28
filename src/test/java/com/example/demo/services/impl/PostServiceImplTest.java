package com.example.demo.services.impl;

import com.example.demo.domain.entities.Post;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.models.binding.PostAddModel;
import com.example.demo.domain.models.view.PostWithUsernameView;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPost() {

        String username = "testuser";
        PostAddModel postAddModel = new PostAddModel();
        postAddModel.setText("test post text");
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(postRepository.saveAndFlush(Mockito.any())).thenReturn(new Post());

        postService.addPost(postAddModel, username);

        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(postRepository).saveAndFlush(Mockito.any(Post.class));
    }

    @Test
    void testAddPostWithInvalidUsername() {

        String username = "testuser";
        PostAddModel postAddModel = new PostAddModel();
        postAddModel.setText("test post text");
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
            postService.addPost(postAddModel, username);
        });
    }

    @Test
    void testFindAll() {

        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        Post post1 = new Post("text1", user1);
        Post post2 = new Post("text2", user2);
        List<Post> posts = Arrays.asList(post1, post2);
        Mockito.when(postRepository.findAll()).thenReturn(posts);
        PostWithUsernameView view1 = new PostWithUsernameView();
        view1.setId(post1.getId());
        view1.setText(post1.getText());
        view1.setUsername(user1.getUsername());
        PostWithUsernameView view2 = new PostWithUsernameView();
        view2.setId(post2.getId());
        view2.setText(post2.getText());
        view2.setUsername(user2.getUsername());


        List<PostWithUsernameView> result = postService.findAll();

        Assertions.assertEquals(2,result.size());
        Assertions.assertEquals(posts.get(0).getText(),result.get(0).getText());
        Assertions.assertEquals(posts.get(1).getText(),result.get(1).getText());
    }

    @Test
    void testRemovePostById() {

        String postId = "testid";

        postService.removePostById(postId);

        Mockito.verify(postRepository).deleteById(postId);
    }

}
