package com.HimanshuBagga.SpringSecurity.SpringSecurity.Controller;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.PostDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor

public class PostController {
    private final PostService postService;

    @GetMapping
    @Secured("ROLE_USER")
    public List<PostDTO> getAllPost(){
        return postService.getAllPost();
    }

    @GetMapping("/{postId}")
    public PostDTO getPostById(@PathVariable Long postId){
        return postService.getPostById(postId);
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost){
        return postService.createNewPost(inputPost);
    }

    @PutMapping("/{postId}")
    public PostDTO updatePost(@RequestBody PostDTO postDTO , @PathVariable Long postId){
        return postService.updatePost(postDTO , postId);
    }
}
