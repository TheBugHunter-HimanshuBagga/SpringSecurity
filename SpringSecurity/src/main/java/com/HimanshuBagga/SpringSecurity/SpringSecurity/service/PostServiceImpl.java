package com.HimanshuBagga.SpringSecurity.SpringSecurity.service;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.Exception.ResourceNotFoundException;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.Repository.PostRepository;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.dto.PostDTO;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.PostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<PostDTO> getAllPost() {
        return postRepository.findAll()
                .stream()
                .map(postEntity -> modelMapper.map(postEntity , PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO createNewPost(PostDTO inputPost) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // get the current user
        PostEntity postEntity = modelMapper.map(inputPost , PostEntity.class);
        postEntity.setAuthor(user);
        var createdNewPost = postRepository.save(postEntity);
        return modelMapper.map(createdNewPost , PostDTO.class);
    }

    @Override
    public PostDTO getPostById(Long postId) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("user {}", user);
        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() ->new ResourceNotFoundException("Resource Not Found with id: " + postId));
        return modelMapper.map(postEntity , PostDTO.class);
    }


    @Override
    public PostDTO updatePost(PostDTO postDTO, Long postId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->new ResourceNotFoundException("Resource Not Found with id: " + postId));
        postDTO.setId(postId);
        modelMapper.map(postDTO , postEntity); // copy data from postDTO to postEntity
        PostEntity postEntity1 = postRepository.save(postEntity);
        return modelMapper.map(postEntity1 , PostDTO.class);
    }
}
