package com.HimanshuBagga.SpringSecurity.SpringSecurity.service;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPost();
    PostDTO createNewPost(PostDTO inputPost);
    PostDTO getPostById(Long postId);

    PostDTO updatePost(PostDTO postDTO, Long postId);
}
