package com.example.imageservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Image extends BaseEntity{
    @Column(name = "url", length = 9999)
    private String url;

    @ManyToOne
    @JoinColumn(name = "customer")
    @JsonBackReference
    private Customer customer;
}
