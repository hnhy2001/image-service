package com.example.imageservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "card")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Card extends BaseEntity{
    @Column(name = "bank")
    private String bank;

    @Column(name = "level")
    private Long level;

    @Column(name = "statement_date")
    private Long statementDate;

    @Column(name = "payment_date")
    private Long paymentDate;

    @Column(name = "payment_feeds")
    private Long paymentFeeds;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImageCard> images;
}
