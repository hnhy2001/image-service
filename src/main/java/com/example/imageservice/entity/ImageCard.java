package com.example.imageservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "image_card")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ImageCard extends BaseEntity{
    @Column(name = "url", length = 9999)
    private String url;

    @ManyToOne
    @JoinColumn(name = "card")
    @JsonBackReference
    private Card card;
}
