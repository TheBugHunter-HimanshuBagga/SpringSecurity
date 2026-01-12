package com.HimanshuBagga.SpringSecurity.SpringSecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    @NotBlank(message = "Title can't be null")
    private String title;
    @NotBlank(message = "Description can't be left empty.")
    @Size(max = 100 , min = 10)
    private String description;
    private LocalDateTime createdAtt;
    private LocalDateTime updatedDate;
    private String createdAt;
    private String updatedBy;
}
