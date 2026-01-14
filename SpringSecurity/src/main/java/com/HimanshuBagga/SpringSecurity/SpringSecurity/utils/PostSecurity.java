package com.HimanshuBagga.SpringSecurity.SpringSecurity.utils;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.PostDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.PostEntity;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {
    private final PostService postService;
    public boolean isOwnerOfPost(Long postId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // get the current user
        PostDTO postDTO = postService.getPostById(postId);
        return postDTO.getUserDTO().getId().equals(user.getId());
    }

}
