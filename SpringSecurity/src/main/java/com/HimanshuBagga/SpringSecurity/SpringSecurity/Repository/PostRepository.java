package com.HimanshuBagga.SpringSecurity.SpringSecurity.Repository;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity , Long> {
}
